#include "../util/jniglue.h"

enum CBLAS_ORDER {CblasRowMajor=101, CblasColMajor=102};
enum CBLAS_TRANSPOSE {CblasNoTrans=111, CblasTrans=112, CblasConjTrans=113};
enum CBLAS_UPLO {CblasUpper=121, CblasLower=122};
enum CBLAS_DIAG {CblasNonUnit=131, CblasUnit=132};
enum CBLAS_SIDE {CblasLeft=141, CblasRight=142};

extern "C" void cblas_dgemm(
  const enum CBLAS_ORDER order, 
  const enum CBLAS_TRANSPOSE transa, 
  const enum CBLAS_TRANSPOSE transb, 
  const int m, const int n, const int k, 
  const double alpha, const double *a, const int lda, 
  const double *b, const int ldb,
  const double beta, double *c, const int ldc);

extern "C" void cblas_dtrsm(
    const enum CBLAS_ORDER order, 
    const enum CBLAS_SIDE side, 
    const enum CBLAS_UPLO uplo, 
    const enum CBLAS_TRANSPOSE trans, 
    const enum CBLAS_DIAG diag, 
    const int m, const int n, 
    const double alpha, const double* a, const int lda, 
    double* b, const int ldb);

extern "C" JNIEXPORT void JNICALL
Java_edu_mines_jtk_lapack_Blas_dgemm(
  JNIEnv* env, jclass cls,
  jint order, jint transa, jint transb, jint m, jint n, jint k,
  jdouble alpha, jdoubleArray ja, jint lda, jdoubleArray jb, jint ldb,
  jdouble beta, jdoubleArray jc, jint ldc)
{
  JNI_TRY
  JdoubleArray a(env,ja);
  JdoubleArray b(env,jb);
  JdoubleArray c(env,jc);
  cblas_dgemm(
    (CBLAS_ORDER)order,
    (CBLAS_TRANSPOSE)transa,
    (CBLAS_TRANSPOSE)transb,
    m,n,k,alpha,a,lda,b,ldb,beta,c,ldc);
  JNI_CATCH
}

extern "C" JNIEXPORT void JNICALL
Java_edu_mines_jtk_lapack_Blas_dtrsm(
  JNIEnv* env, jclass cls,
  jint order, jint side, jint uplo, jint trans, jint diag, 
  jint m, jint n, 
  jdouble alpha, jdoubleArray ja, jint lda, 
  jdoubleArray jb, jint ldb)
{
  JNI_TRY
  JdoubleArray a(env,ja);
  JdoubleArray b(env,jb);
  cblas_dtrsm(
    (CBLAS_ORDER)order,
    (CBLAS_SIDE)side,
    (CBLAS_UPLO)uplo,
    (CBLAS_TRANSPOSE)trans,
    (CBLAS_DIAG)diag,
    m,n,alpha,a,lda,b,ldb);
  JNI_CATCH
}
