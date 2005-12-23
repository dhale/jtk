#include "../util/jniglue.h"
#include <sstream>

static jint check(JNIEnv* env, int info) {
  if (info<0) {
    std::ostringstream ss;
    ss << "illegal value for argument number " << (-info);
    env->ThrowNew(env->FindClass(
      "java/lang/IllegalArgumentException"),ss.str().c_str());
  }
  return info;
}

// From cblas.h; must be consistent with Blas.java.
//enum CBLAS_ORDER {CblasRowMajor=101, CblasColMajor=102};
//enum CBLAS_TRANSPOSE {CblasNoTrans=111, CblasTrans=112, CblasConjTrans=113};
//enum CBLAS_UPLO {CblasUpper=121, CblasLower=122};
//enum CBLAS_DIAG {CblasNonUnit=131, CblasUnit=132};
//enum CBLAS_SIDE {CblasLeft=141, CblasRight=142};
inline static char* ORDER(jint order) {
  static char* s[2] = {"R","C"};
  return s[order-101];
}
inline static char* TRANS(jint trans) {
  static char* s[3] = {"N","T","C"};
  return s[trans-111];
}
inline static char* UPLO(jint uplo) {
  static char* s[2] = {"U","L"};
  return s[uplo-121];
}
inline static char* DIAG(jint diag) {
  static char* s[2] = {"N","U"};
  return s[diag-131];
}
inline static char* SIDE(jint side) {
  static char* s[2] = {"L","R"};
  return s[side-141];
}

// Not BLAS, but needed for some LAPACK subroutines.
inline static char* JOB(jint job) {
  static char* s[5] = {"A","S","O","N","V"};
  return s[job-201];
}
inline static char* RANGE(jint range) {
  static char* s[3] = {"A","V","I"};
  return s[range-301];
}

/////////////////////////////////////////////////////////////////////////////
// LU decomposition

extern "C" int dgetrf_(
  int *m, int *n, double *a, int *lda, int *ipiv, int *info);

extern "C" int dgetrs_(
  char *trans, int *n, int *nrhs, 
  double *a, int *lda, int *ipiv, 
  double *b, int *ldb, int *info);
 
extern "C" JNIEXPORT jint JNICALL
Java_edu_mines_jtk_lapack_Lapack_dgetrf(
  JNIEnv* env, jclass cls,
  jint jm, jint jn, jdoubleArray ja, jint jlda, jintArray jipiv)
{
  JNI_TRY
  int m = jm;
  int n = jn;
  JdoubleArray a(env,ja);
  int lda = jlda;
  JintArray ipiv(env,jipiv);
  int info;
  dgetrf_(&m,&n,a,&lda,ipiv,&info);
  return check(env,info);
  JNI_CATCH
}

extern "C" JNIEXPORT jint JNICALL
Java_edu_mines_jtk_lapack_Lapack_dgetrs(
  JNIEnv* env, jclass cls,
  jint jtrans, jint jn, jint jnrhs, 
  jdoubleArray ja, jint jlda, jintArray jipiv, 
  jdoubleArray jb, jint jldb)
{
  JNI_TRY
  char* trans = TRANS(jtrans);
  int n = jn;
  int nrhs = jnrhs;
  JdoubleArray a(env,ja);
  int lda = jlda;
  JintArray ipiv(env,jipiv);
  JdoubleArray b(env,jb);
  int ldb = jldb;
  int info;
  dgetrs_(trans,&n,&nrhs,a,&lda,ipiv,b,&ldb,&info);
  return check(env,info);
  JNI_CATCH
}

/////////////////////////////////////////////////////////////////////////////
// Cholesky decomposition

extern "C" int dpotrf_(
  char *uplo, int *n, double *a, int *lda, int *info);

extern "C" int dpotrs_(
  char *uplo, int *n, int *nrhs, 
  double *a, int *lda, 
  double *b, int *ldb, int *info);
 
extern "C" JNIEXPORT jint JNICALL
Java_edu_mines_jtk_lapack_Lapack_dpotrf(
  JNIEnv* env, jclass cls,
  jint juplo, jint jn, jdoubleArray ja, jint jlda)
{
  JNI_TRY
  char* uplo = UPLO(juplo);
  int n = jn;
  JdoubleArray a(env,ja);
  int lda = jlda;
  int info;
  dpotrf_(uplo,&n,a,&lda,&info);
  return check(env,info);
  JNI_CATCH
}

extern "C" JNIEXPORT jint JNICALL
Java_edu_mines_jtk_lapack_Lapack_dpotrs(
  JNIEnv* env, jclass cls,
  jint juplo, jint jn, jint jnrhs, 
  jdoubleArray ja, jint jlda,
  jdoubleArray jb, jint jldb)
{
  JNI_TRY
  char* uplo = UPLO(juplo);
  int n = jn;
  int nrhs = jnrhs;
  JdoubleArray a(env,ja);
  int lda = jlda;
  JdoubleArray b(env,jb);
  int ldb = jldb;
  int info;
  dpotrs_(uplo,&n,&nrhs,a,&lda,b,&ldb,&info);
  return check(env,info);
  JNI_CATCH
}

/////////////////////////////////////////////////////////////////////////////
// QR decomposition

extern "C" int dgeqrf_(
  int *m, int *n, double *a, int *lda, 
  double *tau, double *work, int *lwork, int *info);

extern "C" int dorgqr_(
  int *m, int *n, int *k, double *a, int *lda, 
  double *tau, double *work, int *lwork, int *info);

extern "C" int dormqr_(
  char* side, char* trans, int *m, int *n, int *k, 
  double *a, int *lda, double *tau, double* c, int *ldc, 
  double *work, int *lwork, int *info);

extern "C" int dtrtrs_(
  char *uplo, char *trans, char *diag, 
  int *n, int *nrhs, double *a, int *lda, double *b, int *ldb, int *info);
 
extern "C" JNIEXPORT jint JNICALL
Java_edu_mines_jtk_lapack_Lapack_dgeqrf(
  JNIEnv* env, jclass cls,
  jint jm, jint jn, jdoubleArray ja, jint jlda, 
  jdoubleArray jtau, jdoubleArray jwork, jint jlwork)
{
  JNI_TRY
  int m = jm;
  int n = jn;
  JdoubleArray a(env,ja);
  int lda = jlda;
  JdoubleArray tau(env,jtau);
  JdoubleArray work(env,jwork);
  int lwork = jlwork;
  int info;
  dgeqrf_(&m,&n,a,&lda,tau,work,&lwork,&info);
  return check(env,info);
  JNI_CATCH
}
 
extern "C" JNIEXPORT jint JNICALL
Java_edu_mines_jtk_lapack_Lapack_dorgqr(
  JNIEnv* env, jclass cls,
  jint jm, jint jn, jint jk, jdoubleArray ja, jint jlda, 
  jdoubleArray jtau, jdoubleArray jwork, jint jlwork)
{
  JNI_TRY
  int m = jm;
  int n = jn;
  int k = jk;
  JdoubleArray a(env,ja);
  int lda = jlda;
  JdoubleArray tau(env,jtau);
  JdoubleArray work(env,jwork);
  int lwork = jlwork;
  int info;
  dorgqr_(&m,&n,&k,a,&lda,tau,work,&lwork,&info);
  return check(env,info);
  JNI_CATCH
}

extern "C" JNIEXPORT jint JNICALL
Java_edu_mines_jtk_lapack_Lapack_dormqr(
  JNIEnv* env, jclass cls,
  jint jside, jint jtrans, jint jm, jint jn, jint jk, 
  jdoubleArray ja, jint jlda, 
  jdoubleArray jtau, 
  jdoubleArray jc, jint jldc,
  jdoubleArray jwork, jint jlwork)
{
  JNI_TRY
  char* side = SIDE(jside);
  char* trans = TRANS(jtrans);
  int m = jm;
  int n = jn;
  int k = jk;
  JdoubleArray a(env,ja);
  int lda = jlda;
  JdoubleArray tau(env,jtau);
  JdoubleArray c(env,jc);
  int ldc = jldc;
  JdoubleArray work(env,jwork);
  int lwork = jlwork;
  int info;
  dormqr_(side,trans,&m,&n,&k,a,&lda,tau,c,&ldc,work,&lwork,&info);
  return check(env,info);
  JNI_CATCH
}

extern "C" JNIEXPORT jint JNICALL
Java_edu_mines_jtk_lapack_Lapack_dtrtrs(
  JNIEnv* env, jclass cls,
  jint juplo, jint jtrans, jint jdiag, 
  jint jn, jint jnrhs,
  jdoubleArray ja, jint jlda, jdoubleArray jb, jint jldb)
{
  JNI_TRY
  char* uplo = UPLO(juplo);
  char* trans = TRANS(jtrans);
  char* diag = DIAG(jdiag);
  int n = jn;
  int nrhs = jnrhs;
  JdoubleArray a(env,ja);
  int lda = jlda;
  JdoubleArray b(env,jb);
  int ldb = jldb;
  int info;
  dtrtrs_(uplo,trans,diag,&n,&nrhs,a,&lda,b,&ldb,&info);
  return check(env,info);
  JNI_CATCH
}


/////////////////////////////////////////////////////////////////////////////
// Singular value decomposition

extern "C" int dgesvd_(
  char *jobu, char *jobvt, int *m, int *n, double *a, int *lda, 
  double *s, double *u, int *ldu, double *vt, int *ldvt, 
  double *work, int *lwork, int *info);

extern "C" JNIEXPORT jint JNICALL
Java_edu_mines_jtk_lapack_Lapack_dgesvd(
  JNIEnv* env, jclass cls,
  jint jjobu, jint jjobvt, jint jm, jint jn, jdoubleArray ja, jint jlda, 
  jdoubleArray js, jdoubleArray ju, jint jldu, jdoubleArray jvt, jint jldvt,
  jdoubleArray jwork, jint jlwork)
{
  JNI_TRY
  char* jobu = JOB(jjobu);
  char* jobvt = JOB(jjobvt);
  int m = jm;
  int n = jn;
  JdoubleArray a(env,ja);
  int lda = jlda;
  JdoubleArray s(env,js);
  JdoubleArray u(env,ju);
  int ldu = jldu;
  JdoubleArray vt(env,jvt);
  int ldvt = jldvt;
  JdoubleArray work(env,jwork);
  int lwork = jlwork;
  int info;
  dgesvd_(jobu,jobvt,&m,&n,a,&lda,s,u,&ldu,vt,&ldvt,work,&lwork,&info);
  return check(env,info);
  JNI_CATCH
}

/////////////////////////////////////////////////////////////////////////////
// Eigenvalue decomposition

extern "C" int dsyevr_(
  char *jobz, char *range, char *uplo, 
  int *n, double *a, int *lda, double *vl, double *vu, int *il, int *iu, 
  double *abstol, int *m, double *w, double *z, int *ldz, int *isuppz, 
  double *work, int *lwork, int *iwork, int *liwork, int *info);

extern "C" int dgeev_(
  char *jobvl, char *jobvr, 
  int *n, double *a, int *lda, double *wr, double *wi, 
  double *vl, int *ldvl, double *vr, int *ldvr, 
  double *work, int *lwork, int *info);

extern "C" JNIEXPORT jint JNICALL
Java_edu_mines_jtk_lapack_Lapack_dsyevr(
  JNIEnv* env, jclass cls,
  jint jjobz, jint jrange, jint juplo,
  jint jn, jdoubleArray ja, jint jlda, 
  jdouble jvl, jdouble jvu, jint jil, jint jiu,
  jdouble jabstol, jintArray jm, jdoubleArray jw, 
  jdoubleArray jz, jint jldz, jintArray jisuppz,
  jdoubleArray jwork, jint jlwork, jintArray jiwork, jint jliwork)
{
  JNI_TRY
  char* jobz = JOB(jjobz);
  char* range = RANGE(jrange);
  char* uplo = UPLO(juplo);
  int n = jn;
  JdoubleArray a(env,ja);
  int lda = jlda;
  double vl = jvl;
  double vu = jvu;
  int il = jil;
  int iu = jiu;
  double abstol = jabstol;
  JintArray m(env,jm);
  JdoubleArray w(env,jw);
  JdoubleArray z(env,jz);
  int ldz = jldz;
  JintArray isuppz(env,jisuppz);
  JdoubleArray work(env,jwork);
  int lwork = jlwork;
  JintArray iwork(env,jiwork);
  int liwork = jliwork;
  int info;
  dsyevr_(jobz,range,uplo,&n,a,&lda,&vl,&vu,&il,&iu,
    &abstol,m,w,z,&ldz,isuppz,work,&lwork,iwork,&liwork,&info);
  return check(env,info);
  JNI_CATCH
}

extern "C" JNIEXPORT jint JNICALL
Java_edu_mines_jtk_lapack_Lapack_dgeev(
  JNIEnv* env, jclass cls,
  jint jjobvl, jint jjobvr,
  jint jn, jdoubleArray ja, jint jlda, 
  jdoubleArray jwr, jdoubleArray jwi,
  jdoubleArray jvl, jint jldvl, jdoubleArray jvr, jint jldvr, 
  jdoubleArray jwork, jint jlwork)
{
  JNI_TRY
  char* jobvl = JOB(jjobvl);
  char* jobvr = JOB(jjobvr);
  int n = jn;
  JdoubleArray a(env,ja);
  int lda = jlda;
  JdoubleArray wr(env,jwr);
  JdoubleArray wi(env,jwi);
  JdoubleArray vl(env,jvl);
  int ldvl = jldvl;
  JdoubleArray vr(env,jvr);
  int ldvr = jldvr;
  JdoubleArray work(env,jwork);
  int lwork = jlwork;
  int info;
  dgeev_(jobvl,jobvr,&n,a,&lda,wr,wi,vl,&ldvl,vr,&ldvr,work,&lwork,&info);
  return check(env,info);
  JNI_CATCH
}
