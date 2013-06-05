/****************************************************************************
Copyright (c) 2009, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.interp;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import edu.mines.jtk.dsp.Tensors3;
import edu.mines.jtk.util.Stopwatch;
import static edu.mines.jtk.util.ArrayMath.*;

/**
 * EXPERIMENTAL: uses a heap instead of random shuffling of known samples.
 * <p>
 * A time and closest-point transform for 3D anisotropic eikonal equations.
 * Transforms an array of times and marks for known samples into an array 
 * of times and marks for all samples. Known samples are those for which
 * times are zero, and times and marks for known samples are not modified.
 * <p>
 * Times for unknown samples are computed by solving an anisotropic eikonal 
 * equation grad(t) dot W grad(t) = 1, where W denotes a positive-definite 
 * (velocity-squared) metric tensor field. The solution times t represent 
 * the traveltimes from one known sample to all unknown samples. Separate 
 * solution times t are computed for each known sample that has at least 
 * one unknown neighbor. (Such a known sample is sometimes called a 
 * "source point.") The output time for each sample is the minimum time 
 * computed in this way for all such known samples. Therefore, the times 
 * output for unknown samples are the traveltimes to the closest known 
 * samples, where "closest" here means least time, not least distance.
 * <p>
 * As eikonal solutions t are computed for each known sample, the mark
 * for that known sample is used to mark all unknown samples for which
 * the solution time is smaller than the minimum time computed so far.
 * If marks for known samples are distinct, then output marks for unknown
 * samples indicate which known sample is closest. In this way output
 * marks can represent a sampled Voronoi diagram, albeit one that has 
 * been generalized by replacing distance with time.
 * <p>
 * This transform uses an iterative sweeping method to solve for times.
 * Iterations are similar to those described by Jeong and Whitaker (2007).
 * Computational complexity is O(M log K), where M is the number of 
 * unknown (missing) samples and K is the number of known samples.
 * @author Dave Hale, Colorado School of Mines
 * @version 2009.07.21
 */
class TimeMarker3X {

  /**
   * Type of concurrency used by this transform.
   */
  public enum Concurrency {
    PARALLEL,
    SERIAL
  }
  
  /**
   * Constructs a time marker for the specified tensor field.
   * @param n1 number of samples in 1st dimension.
   * @param n2 number of samples in 2nd dimension.
   * @param n3 number of samples in 2nd dimension.
   * @param tensors velocity-squared tensors.
   */
  public TimeMarker3X(int n1, int n2, int n3, Tensors3 tensors) {
    init(n1,n2,n3,tensors);
  }

  /**
   * Sets the tensors used by this time marker.
   * @param tensors the tensors.
   */
  public void setTensors(Tensors3 tensors) {
    _tensors = tensors;
  }

  /**
   * Sets the type of concurrency used to solve for times.
   * The default concurrency is parallel.
   * @param concurrency the type of concurrency.
   */
  public void setConcurrency(Concurrency concurrency) {
    _concurrency = concurrency;
  }

  /**
   * Transforms the specified array of times and marks.
   * Known samples are those for which times are zero, and times
   * and marks for these known samples are used to compute times
   * and marks for unknown samples.
   * @param times input/output array of times.
   * @param marks input/output array of marks.
   */
  public void apply(float[][][] times, int[][][] marks) {

    // Measure elapsed time in seconds.
    Stopwatch sw = new Stopwatch();
    sw.start();
    log.fine("TimeMarker3X.apply: begin time="+(int)sw.time());

    // Make a heap of the known samples.
    TimeHeap3 theap = makeTimeHeap(times,marks);
    int nk = theap.size();
    log.fine("TimeMarker3X.apply: heap time="+(int)sw.time());

    // Initialize all times to infinity.
    for (int i3=0; i3<_n3; ++i3) {
      for (int i2=0; i2<_n2; ++i2) {
        for (int i1=0; i1<_n1; ++i1) {
          times[i3][i2][i1] = INFINITY;
        }
      }
    }

    // Array for the eikonal solution times.
    float[][][] t = new float[_n3][_n2][_n1];

    // Active list of samples used to compute times.
    ActiveList al = new ActiveList();

    // For all known samples, ...
    for (int ik=0; ik<nk; ++ik) {
      if (ik%(1+(nk-1)/100)==0)
        log.fine("  apply: ik/nk="+ik+"/"+nk+" time="+(int)sw.time());

      // Remove known sample with largest time from the heap.
      TimeHeap3.Entry ek = theap.remove();
      int k1 = ek.i1;
      int k2 = ek.i2;
      int k3 = ek.i3;
      int m = ek.mark;

      // Known samples have zero time and specified mark.
      times[k3][k2][k1] = 0.0f;
      marks[k3][k2][k1] = m;

      // Clear activated flags so we can tell which samples become activated.
      clearActivated();

      // Put the known sample with time zero into the active list.
      t[k3][k2][k1] = 0.0f;
      al.append(_s[k3][k2][k1]);

      // Process the active list until empty.
      solve(al,t,m,times,marks);

      // Update the time heap.
      updateTimeHeap(times,marks,ek,theap);
    }

    // Log elapsed time.
    sw.stop();
    log.fine("TimeMarker3X.apply: end time="+(int)sw.time());
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private static Logger log = 
    Logger.getLogger(NearestGridder3.class.getName());

  // Default time for samples not yet computed.
  private static final float INFINITY = Float.MAX_VALUE;

  // Times are converged when the fractional change is less than this value.
  private static final float EPSILON = 0.001f;
  private static final float ONE_MINUS_EPSILON = 1.0f-EPSILON;

  private int _n1,_n2,_n3;
  private Tensors3 _tensors;
  private Sample[][][] _s;
  private ArrayList<Sample> _als = new ArrayList<Sample>(2048);
  private Concurrency _concurrency = Concurrency.PARALLEL;

  private void init(int n1, int n2, int n3, Tensors3 tensors) {
    _n1 = n1;
    _n2 = n2;
    _n3 = n3;
    _tensors = tensors;
    _s = new Sample[n3][n2][n1];
    for (int i3=0; i3<n3; ++i3)
      for (int i2=0; i2<n2; ++i2)
        for (int i1=0; i1<n1; ++i1)
          _s[i3][i2][i1] = new Sample(i1,i2,i3);
  }

  // Sample index offsets for six neighbor samples.
  // Must be consistent with the neighbor sets below.
  private static final int[] K1 = {-1, 1, 0, 0, 0, 0};
  private static final int[] K2 = { 0, 0,-1, 1, 0, 0};
  private static final int[] K3 = { 0, 0, 0, 0,-1, 1};

  // Sets of neighbor sample offsets used to compute times. These must
  // be consistent with the offsets above. For example, when updating the 
  // neighbor with offsets {K1[1],K2[1],K3[1]} = {1,0,0}, only the sets 
  // K1S[1], K2S[1], and K3S[1] are used. The sets K1S[6], K2S[6], and 
  // K3S[6] are special offsets for all six neighbors. Indices in each
  // set are ordered so that tets are first, tris next, and edges last.
  // Tets are defined by three non-zero offsets, tris by two, and edges
  // by one.
  private static final int[][] K1S = {
    { 1, 1, 1, 1, 1, 1, 1, 1, 1}, // A
    {-1,-1,-1,-1,-1,-1,-1,-1,-1}, // A
    {-1, 1,-1, 1,-1, 1, 0, 0, 0}, // B
    {-1, 1,-1, 1,-1, 1, 0, 0, 0}, // B
    {-1,-1, 1, 1, 0, 0,-1, 1, 0}, // C
    {-1,-1, 1, 1, 0, 0,-1, 1, 0}, // C
    {-1, 1,-1, 1,-1, 1,-1, 1,             //    8 tets 
     -1, 1,-1, 1,-1, 1,-1, 1, 0, 0, 0, 0, // + 12 tris
     -1, 1, 0, 0, 0, 0}};                 // +  6 edges = 26 cases
  private static final int[][] K2S = {
    {-1,-1, 1, 1, 0, 0,-1, 1, 0}, // C
    {-1,-1, 1, 1, 0, 0,-1, 1, 0}, // C
    { 1, 1, 1, 1, 1, 1, 1, 1, 1}, // A
    {-1,-1,-1,-1,-1,-1,-1,-1,-1}, // A
    {-1, 1,-1, 1,-1, 1, 0, 0, 0}, // B
    {-1, 1,-1, 1,-1, 1, 0, 0, 0}, // B
    {-1,-1, 1, 1,-1,-1, 1, 1,
     -1,-1, 1, 1, 0, 0, 0, 0,-1, 1,-1, 1,
      0, 0,-1, 1, 0, 0}};
  private static final int[][] K3S = {
    {-1, 1,-1, 1,-1, 1, 0, 0, 0}, // B
    {-1, 1,-1, 1,-1, 1, 0, 0, 0}, // B
    {-1,-1, 1, 1, 0, 0,-1, 1, 0}, // C
    {-1,-1, 1, 1, 0, 0,-1, 1, 0}, // C
    { 1, 1, 1, 1, 1, 1, 1, 1, 1}, // A
    {-1,-1,-1,-1,-1,-1,-1,-1,-1}, // A
    {-1,-1,-1,-1, 1, 1, 1, 1,
      0, 0, 0, 0,-1,-1, 1, 1,-1,-1, 1, 1,
      0, 0, 0, 0,-1, 1}};

  // A sample has indices and a flag used to build the active list.
  private static class Sample {
    int i1,i2,i3; // sample indices
    int activated; // used to flag activated samples
    boolean absent; // used to build active lists
    Sample(int i1, int i2, int i3) {
      this.i1 = i1;
      this.i2 = i2;
      this.i3 = i3;
    }
  }

  // List of active samples.
  private class ActiveList {
    void append(Sample s) {
      s.activated = _activated;
      if (_n==_a.length)
        growTo(2*_n);
      _a[_n++] = s;
    }
    boolean isEmpty() {
      return _n==0;
    }
    int size() {
      return _n;
    }
    Sample get(int i) {
      return _a[i];
    }
    void clear() {
      _n = 0;
    }
    void setAllAbsent() {
      for (int i=0; i<_n; ++i)
        _a[i].absent = true;
    }
    void appendIfAbsent(ActiveList al) {
      if (_n+al._n>_a.length)
        growTo(2*(_n+al._n));
      int n = al._n;
      for (int i=0; i<n; ++i) {
        Sample s = al.get(i);
        if (s.absent) {
          _a[_n++] = s;
          s.absent = false;
        }
      }
    }
    void shuffle() { // experiment: randomizes order of samples in this list
      Random r = new Random();
      for (int i=0; i<_n; ++i) {
        int j = r.nextInt(_n);
        int k = r.nextInt(_n);
        Sample aj = _a[j];
        _a[j] = _a[k];
        _a[k] = aj;
      }
    }
    void dump() { // debugging: prints this list
      trace("ActiveList.dump: n="+_n);
      for (int i=0; i<_n; ++i) {
        Sample s = _a[i];
        trace(" s["+i+"] = ("+s.i1+","+s.i2+","+s.i3+")");
      }
    }
    private int _n;
    private Sample[] _a = new Sample[1024];
    private void growTo(int capacity) {
      Sample[] a = new Sample[capacity];
      System.arraycopy(_a,0,a,0,_n);
      _a = a;
    }
  }

  // Flags set during computation of times. For efficiency, do not
  // loop over all the flags to clear them before computing times.
  // Instead, modify the value that represents activated samples.
  private int _activated = 1;
  private void clearActivated() {
    if (_activated==Integer.MAX_VALUE) { // rarely!
      _activated = 1;
      for (int i3=0; i3<_n3; ++i3) {
        for (int i2=0; i2<_n2; ++i2) {
          for (int i1=0; i1<_n1; ++i1) {
            _s[i3][i2][i1].activated = 0;
          }
        }
      }
    } else { // typically
      ++_activated;
    }
  }
  private void setActivated(Sample s) {
    s.activated = _activated;
  }
  private void clearActivated(Sample s) {
    s.activated = 0;
  }
  private boolean wasActivated(Sample s) {
    return s.activated==_activated;
  }

  /*
   * Returns a heap of known samples. At the top of the heap is
   * the known sample nearest to the middle of the sampling grid.
   */
  private TimeHeap3 makeTimeHeap(float[][][] times, int[][][] marks) {

    // Marks and indices of known samples.
    int[][] kk = indexKnownSamples(times,marks);
    int[] km = kk[0];
    int[] k1 = kk[1];
    int[] k2 = kk[2];
    int[] k3 = kk[3];
    int nk = km.length;

    // Find the known sample nearest to the middle of the sampling grid.
    int ikmid = 0;
    float dkmid = INFINITY;
    int m1 = _n1/2;
    int m2 = _n2/2;
    int m3 = _n3/2;
    for (int ik=0; ik<nk; ++ik) {
      int i1 = k1[ik];
      int i2 = k2[ik];
      int i3 = k3[ik];
      float d1 = i1-m1;
      float d2 = i2-m2;
      float d3 = i3-m3;
      float dk = d1*d1+d2*d2+d3*d3;
      if (dk<dkmid) {
        ikmid = ik;
        dkmid = dk;
      }
    }

    // Build a heap of known samples. Ensure that the sample nearest 
    // the middle of the sampling grid is at the top of the heap.
    TimeHeap3 theap = new TimeHeap3(TimeHeap3.Type.MAX,_n1,_n2,_n3);
    for (int ik=0; ik<nk; ++ik) {
      if (ik==ikmid) {
        theap.insert(k1[ik],k2[ik],k3[ik],INFINITY,km[ik]);
      } else {
        theap.insert(k1[ik],k2[ik],k3[ik],0.5f*INFINITY,km[ik]);
      }
    }
    return theap;
  }

  /*
   * Updates the time heap. Should be called after times and marks 
   * have been computed for the known sample with specified entry.
   * This method reduces times for entries in the heap corresponding
   * to known samples that were activated by the solver.
   */
  private void updateTimeHeap(
    float[][][] times, int[][][] marks, TimeHeap3.Entry ek, TimeHeap3 theap)
  {
    if (theap.isEmpty()) 
      return;

    // Indices and mark for the specified entry.
    int k1 = ek.i1;
    int k2 = ek.i2;
    int k3 = ek.i3;
    int m = ek.mark;

    // Clear the list of samples.
    _als.clear();

    // Add the known sample to the list.
    addSampleToList(k1,k2,k3);

    // While the list of samples is not empty, ...
    while (!_als.isEmpty()) {

      // Get the next sample from the list.
      Sample s = _als.remove(_als.size()-1);
      int i1 = s.i1;
      int i2 = s.i2;
      int i3 = s.i3;

      // If sample has the current mark and is known, reduce its time.
      if (m==marks[i3][i2][i1] && theap.contains(i1,i2,i3))
        theap.reduce(i1,i2,i3,times[i3][i2][i1]);

      // Add to the list any neighbor samples that were activated.
      if (0<i1) addSampleToList(i1-1,i2,i3);
      if (0<i2) addSampleToList(i1,i2-1,i3);
      if (0<i3) addSampleToList(i1,i2,i3-1);
      if (i1<_n1-1) addSampleToList(i1+1,i2,i3);
      if (i2<_n2-1) addSampleToList(i1,i2+1,i3);
      if (i3<_n3-1) addSampleToList(i1,i3,i3+1);
    }
  }
  private void addSampleToList(int i1, int i2, int i3) {
    if (wasActivated(_s[i3][i2][i1])) {
      _als.add(_s[i3][i2][i1]);
      clearActivated(_s[i3][i2][i1]);
    }
  }

  /*
   * Returns marks and indices of known samples with times zero.
   */
  private int[][] indexKnownSamples(float[][][] times, int[][][] marks) {
    int nk = 0;
    for (int i3=0; i3<_n3; ++i3) {
      for (int i2=0; i2<_n2; ++i2) {
        for (int i1=0; i1<_n1; ++i1) {
          if (times[i3][i2][i1]==0.0f)
            ++nk;
        }
      }
    }
    int[] km = new int[nk];
    int[] k1 = new int[nk];
    int[] k2 = new int[nk];
    int[] k3 = new int[nk];
    for (int i3=0,ik=0; i3<_n3; ++i3) {
      for (int i2=0; i2<_n2; ++i2) {
        for (int i1=0; i1<_n1; ++i1) {
          if (times[i3][i2][i1]==0.0f) {
            km[ik] = marks[i3][i2][i1];
            k1[ik] = i1;
            k2[ik] = i2;
            k3[ik] = i3;
            ++ik;
          }
        }
      }
    }
    return new int[][]{km,k1,k2,k3};
  }

  /*
   * Solves for times and marks.
   */
  private void solve(
    ActiveList al, float[][][] t, int m, float[][][] times, int[][][] marks) 
  {
    if (_concurrency==Concurrency.PARALLEL) {
      solveParallel(al,t,m,times,marks);
    } else {
      solveSerial(al,t,m,times,marks);
    }
  }

  /*
   * Solves for times by sequentially processing each sample in active list.
   */
  private void solveSerial(
    ActiveList al, 
    float[][][] t, int m, 
    float[][][] times, int[][][] marks) 
  {
    float[] d = new float[6];
    ActiveList bl = new ActiveList();
    int ntotal = 0;
    while (!al.isEmpty()) {
      //al.shuffle(); // demonstrate that solution depends on order
      int n = al.size();
      ntotal += n;
      for (int i=0; i<n; ++i) {
        Sample s = al.get(i);
        solveOne(t,m,times,marks,s,bl,d);
      }
      bl.setAllAbsent();
      al.clear();
      al.appendIfAbsent(bl);
      bl.clear();
    }
    trace("solveSerial: ntotal="+ntotal);
    trace("             nratio="+(float)ntotal/(float)(_n1*_n2*_n3));
  }
  
  /*
   * Solves for times by processing samples in the active list in parallel.
   */
  private void solveParallel(
    final ActiveList al,
    final float[][][] t, final int m,
    final float[][][] times, final int[][][] marks)
  {
    int nthread = Runtime.getRuntime().availableProcessors();
    ExecutorService es = Executors.newFixedThreadPool(nthread);
    CompletionService<Void> cs = new ExecutorCompletionService<Void>(es);
    ActiveList[] bl = new ActiveList[nthread];
    float[][] d = new float[nthread][];
    for (int ithread=0; ithread<nthread; ++ithread) {
      bl[ithread] = new ActiveList();
      d[ithread] = new float[6];
    }
    final AtomicInteger ai = new AtomicInteger();
    //int ntotal = 0;
    //int niter = 0;
    while (!al.isEmpty()) {
      ai.set(0); // initialize the shared block index to zero
      final int n = al.size(); // number of samples in active (A) list
      //ntotal += n;
      final int mb = 32; // size of blocks of samples
      final int nb = 1+(n-1)/mb; // number of blocks of samples
      int ntask = min(nb,nthread); // number of tasks (threads to be used)
      for (int itask=0; itask<ntask; ++itask) { // for each task, ...
        final ActiveList bltask = bl[itask]; // task-specific B list 
        final float[] dtask = d[itask]; // task-specific work array
        cs.submit(new Callable<Void>() { // submit new task
          public Void call() {
            for (int ib=ai.getAndIncrement(); ib<nb; ib=ai.getAndIncrement()) {
              int i = ib*mb; // beginning of block
              int j = min(i+mb,n); // beginning of next block (or end)
              for (int k=i; k<j; ++k) { // for each sample in block, ...
                Sample s = al.get(k); // get k'th sample from A list
                solveOne(t,m,times,marks,s,bltask,dtask); // process sample
              }
            }
            bltask.setAllAbsent(); // needed when merging B lists below
            return null;
          }
        });
      }
      try {
        for (int itask=0; itask<ntask; ++itask)
          cs.take();
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }

      // Merge samples from all B lists to a new A list. As samples
      // are appended, their absent flags are set to false, so that 
      // each sample is appended no more than once to the new A list.
      al.clear();
      for (int itask=0; itask<ntask; ++itask) {
        al.appendIfAbsent(bl[itask]);
        bl[itask].clear();
      }
      //++niter;
    }
    es.shutdown();
    //trace("solveParallel: ntotal="+ntotal);
    //trace("               nratio="+(float)ntotal/(float)(_n1*_n2*_n3));
  }

  /*
   * Gets the current times during one solution of the eikonal equation.
   * Times for samples not yet activated are infinite.
   */
  private float currentTime(float[][][] t, int i1, int i2, int i3) {
    return wasActivated(_s[i3][i2][i1])?t[i3][i2][i1]:INFINITY;
  }

  /*
   * Processes one sample from the A list.
   * Appends samples not yet converged to the B list.
   */
  private void solveOne(
    float[][][] t, int m, float[][][] times, int[][][] marks,
    Sample s, ActiveList bl, float[] d) 
  {
    // Sample indices.
    int i1 = s.i1;
    int i2 = s.i2;
    int i3 = s.i3;

    // Current time and new time computed from all four neighbors.
    float ti = currentTime(t,i1,i2,i3);
    float ci = computeTime(t,i1,i2,i3,K1S[6],K2S[6],K3S[6],d);
    t[i3][i2][i1] = ci;

    // If new and current times are close enough (converged), then ...
    if (ci>=ti*ONE_MINUS_EPSILON) {

      // Neighbors may need to be activated if computed time is small 
      // relative to the minimum time computed so far. The factor 1.5 
      // improves accuracy for large anisotropy. Cost increases as the
      // square of this factor, so we do not want it to be too large.
      boolean checkNabors = ci<=1.5f*times[i3][i2][i1];

      // If computed time less than minimum time, mark this sample.
      if (ci<times[i3][i2][i1]) {
        times[i3][i2][i1] = ci;
        marks[i3][i2][i1] = m;
      }

      // If necessary, check the neighbors.
      if (checkNabors) {

        // For all six neighbors, ...
        for (int k=0; k<6; ++k) {

          // Neighbor sample indices; skip if out of bounds.
          int j1 = i1+K1[k];  if (j1<0 || j1>=_n1) continue;
          int j2 = i2+K2[k];  if (j2<0 || j2>=_n2) continue;
          int j3 = i3+K3[k];  if (j3<0 || j3>=_n3) continue;

          // Skip neighbor sample if computed time would be too big.
          //if (!doComputeTime(t,times,j1,j2)) continue;

          // Current and computed times for the neighbor.
          float tj = currentTime(t,j1,j2,j3);
          float cj = computeTime(t,j1,j2,j3,K1S[k],K2S[k],K3S[k],d);

          // If computed time is significantly less than current time, ...
          if (cj<tj*ONE_MINUS_EPSILON) {

            // Replace the current time.
            t[j3][j2][j1] = cj;
            
            // Append neighbor to the B list, thereby activating it.
            bl.append(_s[j3][j2][j1]);
          }
        }
      }
    }

    // Else, if not converged, append this sample to the B list.
    else {
      bl.append(s);
    }
  }

  /*
   * Determines whether to compute time for sample with specified indices.
   * A sample should be processed iff at least one of its neighbors is 
   * less than the minimum time computed so far.
   */
  /*
  private boolean doComputeTime(
    float[][][] t, float[][][] times, int i1, int i2, int i3) 
  {
    float timei = 2.0f*times[i3][i2][i1];
    return t1m(t,i1,i2,i3)<=timei ||
           t1p(t,i1,i2,i3)<=timei ||
           t2m(t,i1,i2,i3)<=timei ||
           t2p(t,i1,i2,i3)<=timei ||
           t3m(t,i1,i2,i3)<=timei ||
           t3p(t,i1,i2,i3)<=timei;
  }
  */

  // Methods to get times for neighbors.
  private float t1m(float[][][] t, int i1, int i2, int i3) {
    return (--i1>=0 && wasActivated(_s[i3][i2][i1]))?t[i3][i2][i1]:INFINITY;
  }
  private float t1p(float[][][] t, int i1, int i2, int i3) {
    return (++i1<_n1 && wasActivated(_s[i3][i2][i1]))?t[i3][i2][i1]:INFINITY;
  }
  private float t2m(float[][][] t, int i1, int i2, int i3) {
    return (--i2>=0 && wasActivated(_s[i3][i2][i1]))?t[i3][i2][i1]:INFINITY;
  }
  private float t2p(float[][][] t, int i1, int i2, int i3) {
    return (++i2<_n2 && wasActivated(_s[i3][i2][i1]))?t[i3][i2][i1]:INFINITY;
  }
  private float t3m(float[][][] t, int i1, int i2, int i3) {
    return (--i3>=0 && wasActivated(_s[i3][i2][i1]))?t[i3][i2][i1]:INFINITY;
  }
  private float t3p(float[][][] t, int i1, int i2, int i3) {
    return (++i3<_n3 && wasActivated(_s[i3][i2][i1]))?t[i3][i2][i1]:INFINITY;
  }

  /*
   * Returns a time t not greater than the current time for one sample.
   * Computations are limited to neighbor samples with specified offsets.
   */
  private float computeTime(
    float[][][] t,
    int i1, int i2, int i3, int[] k1s, int[] k2s, int[] k3s, float[] d) 
  {
    _tensors.getTensor(i1,i2,i3,d);
    float d11 = d[0];
    float d12 = d[1];
    float d13 = d[2];
    float d22 = d[3];
    float d23 = d[4];
    float d33 = d[5];
    float o11 = 1.0f/d11;
    float o22 = 1.0f/d22;
    float o33 = 1.0f/d33;
    float d1212 = d12*d12;
    float d1213 = d12*d13;
    float d1223 = d12*d23;
    float d1313 = d13*d13;
    float d1323 = d13*d23;
    float d2323 = d23*d23;
    float a11 = d11-d1313*o33;
    float a12 = d12-d1323*o33;
    float a22 = d22-d2323*o33;
    float b11 = d11-d1212*o22;
    float b13 = d13-d1223*o22;
    float b33 = d33-d2323*o22;
    float c22 = d22-d1212*o11;
    float c23 = d23-d1213*o11;
    float c33 = d33-d1313*o11;
    float e12 = 1.0f/(a11*a22-a12*a12);
    float e13 = 1.0f/(b11*b33-b13*b13);
    float tc = currentTime(t,i1,i2,i3);
    float t1m = t1m(t,i1,i2,i3);
    float t1p = t1p(t,i1,i2,i3);
    float t2m = t2m(t,i1,i2,i3);
    float t2p = t2p(t,i1,i2,i3);
    float t3m = t3m(t,i1,i2,i3);
    float t3p = t3p(t,i1,i2,i3);
    for (int k=0; k<k1s.length; ++k) {
      int k1 = k1s[k];
      int k2 = k2s[k];
      int k3 = k3s[k];
      float t0,t1,t2,t3;
      if (k1!=0 && k2!=0 && k3!=0) {
        t1 = (k1<0)?t1m:t1p;  if (t1==INFINITY) continue;
        t2 = (k2<0)?t2m:t2p;  if (t2==INFINITY) continue;
        t3 = (k3<0)?t3m:t3p;  if (t3==INFINITY) continue;
        t0 = computeTime(d11,d12,d13,d22,d23,d33,k1,k2,k3,t1,t2,t3);
      } else if (k1!=0 && k2!=0) {
        t1 = (k1<0)?t1m:t1p;  if (t1==INFINITY) continue;
        t2 = (k2<0)?t2m:t2p;  if (t2==INFINITY) continue;
        t0 = computeTime(a11,a12,a22,k1,k2,t1,t2);
      } else if (k1!=0 && k3!=0) {
        t1 = (k1<0)?t1m:t1p;  if (t1==INFINITY) continue;
        t3 = (k3<0)?t3m:t3p;  if (t3==INFINITY) continue;
        t0 = computeTime(b11,b13,b33,k1,k3,t1,t3);
      } else if (k2!=0 && k3!=0) {
        t2 = (k2<0)?t2m:t2p;  if (t2==INFINITY) continue;
        t3 = (k3<0)?t3m:t3p;  if (t3==INFINITY) continue;
        t0 = computeTime(c22,c23,c33,k2,k3,t2,t3);
      } else if (k1!=0) {
        t1 = (k1<0)?t1m:t1p;  if (t1==INFINITY) continue;
        t0 = t1+sqrt(a22*e12);
      } else if (k2!=0) {
        t2 = (k2<0)?t2m:t2p;  if (t2==INFINITY) continue;
        t0 = t2+sqrt(a11*e12);
      } else { // k3!=0
        t3 = (k3<0)?t3m:t3p;  if (t3==INFINITY) continue;
        t0 = t3+sqrt(b11*e13);
      }
      if (t0<tc)
        return t0;
    }
    return tc;
  }

  /*
   * Solves a 3D anisotropic eikonal equation for a positive time t0.
   * The equation is:
   *   d11*s1*s1*(t0-t1)*(t0-t1) + 
   *   d22*s2*s2*(t0-t2)*(t0-t2) +
   *   d33*s3*s3*(t0-t3)*(t0-t3) +
   * 2*d12*s1*s2*(t0-t1)*(t0-t2) + 
   * 2*d13*s1*s3*(t0-t1)*(t0-t3) + 
   * 2*d23*s2*s3*(t0-t2)*(t0-t3) = 1
   * To reduce rounding errors, this method actually solves for u = t0-t1,
   * via the following equation:
   *   ds11*(u    )*(u    ) + 
   *   ds22*(u+t12)*(u+t12) +
   *   ds33*(u+t13)*(u+t13) +
   * 2*ds12*(u    )*(u+t12) + 
   * 2*ds13*(u    )*(u+t13) + 
   * 2*ds23*(u+t12)*(u+t13) = 1
   * If a valid u can be computed, then the time returned is t0 = t1+u.
   * Otherwise, this method returns INFINITY.
   */
  private static float computeTime(
    float d11, float d12, float d13, float d22, float d23, float d33,
    float s1, float s2, float s3, float t1, float t2, float t3) 
  {
    double ds11 = d11*s1*s1;
    double ds22 = d22*s2*s2;
    double ds33 = d33*s3*s3;
    double ds12 = d12*s1*s2;
    double ds13 = d13*s1*s3;
    double ds23 = d23*s2*s3;
    double t12 = t1-t2;
    double t13 = t1-t3;
    double a = ds11+ds22+ds33+2.0*(ds12+ds13+ds23);
    double b = 2.0*((ds22+ds12+ds23)*t12+(ds33+ds13+ds23)*t13);
    double c = ds22*t12*t12+ds33*t13*t13+2.0*ds23*t12*t13-1.0;
    double d = b*b-4.0*a*c;
    if (d<0.0) 
      return INFINITY;
    double u1 = (-b+sqrt(d))/(2.0*a);
    double u2 = u1+t12;
    double u3 = u1+t13;
    if (ds11*u1+ds12*u2+ds13*u3 < 0.0 ||
        ds12*u1+ds22*u2+ds23*u3 < 0.0 ||
        ds13*u1+ds23*u2+ds33*u3 < 0.0)
      return INFINITY;
    return t1+(float)u1;
  }

  /*
   * Solves a 2D anisotropic eikonal equation for a positive time t0.
   * The equation is:
   *   d11*s1*s1*(t1-t0)*(t1-t0) + 
   * 2*d12*s1*s2*(t1-t0)*(t2-t0) + 
   *   d22*s2*s2*(t2-t0)*(t2-t0) = 1
   * To reduce rounding errors, this method actually solves for u = t0-t1,
   * via the following equation:
   *   ds11*(u    )*(u    ) + 
   *   ds22*(u+t12)*(u+t12) +
   * 2*ds12*(u    )*(u+t12) = 1
   * If a valid u can be computed, then the time returned is t0 = t1+u.
   * Otherwise, this method returns INFINITY.
   */
  private static float computeTime(
    float d11, float d12, float d22,
    float s1, float s2, float t1, float t2) 
  {
    double ds11 = d11*s1*s1;
    double ds12 = d12*s1*s2;
    double ds22 = d22*s2*s2;
    double t12 = t1-t2;
    double a = ds11+2.0*ds12+ds22;
    double b = 2.0*(ds12+ds22)*t12;
    double c = ds22*t12*t12-1.0;
    double d = b*b-4.0*a*c;
    if (d<0.0)
      return INFINITY;
    double u1 = (-b+sqrt(d))/(2.0*a);
    double u2 = u1+t12;
    if (ds11*u1+ds12*u2 < 0.0 ||
        ds12*u1+ds22*u2 < 0.0)
      return INFINITY;
    return t1+(float)u1;
  }

  ///////////////////////////////////////////////////////////////////////////
  // debugging

  private static void trace(String s) {
    System.out.println(s);
  }
  private static float[][][] toFloat(int[][][] i) {
    int n1 = i[0][0].length;
    int n2 = i[0].length;
    int n3 = i.length;
    float[][][] f = new float[n3][n2][n1];
    for (int i3=0; i3<n3; ++i3)
      for (int i2=0; i2<n2; ++i2)
        for (int i1=0; i1<n1; ++i1)
          f[i3][i2][i1] = (float)i[i3][i2][i1];
    return f;
  }
}
