package com.diract.auth.repository;

import com.diract.auth.domain.RefreshToken;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {

    private static final String COLLECTION_NAME = "refreshTokens";
    private final Firestore firestore;

    public void save(RefreshToken refreshToken) {
        try {
            ApiFuture<WriteResult> future = firestore
                .collection(COLLECTION_NAME)
                .document(refreshToken.getToken()) // 토큰을 문서 ID로
                .set(refreshToken);

            future.get();
            log.debug("Refresh Token 저장 완료: userId={}", refreshToken.getUserId());
        } catch (InterruptedException | ExecutionException e) {
            log.error("Refresh Token 저장 실패", e);
            throw new RuntimeException("Refresh Token 저장 중 오류 발생", e);
        }
    }

    public Optional<RefreshToken> findByToken(String token) {
        try {
            DocumentSnapshot document = firestore
                .collection(COLLECTION_NAME)
                .document(token)
                .get()
                .get();

            if (!document.exists()) return Optional.empty();

            RefreshToken rt = document.toObject(RefreshToken.class);
            return Optional.ofNullable(rt);
        } catch (InterruptedException | ExecutionException e) {
            log.error("Refresh Token 조회 실패", e);
            throw new RuntimeException("Refresh Token 조회 중 오류 발생", e);
        }
    }

    public Optional<RefreshToken> findByUserId(String userId) {
        try {
            var querySnapshot = firestore
                .collection(COLLECTION_NAME)
                .whereEqualTo("userId", userId)
                .limit(1)
                .get()
                .get();

            if (querySnapshot.isEmpty()) return Optional.empty();

            RefreshToken rt = querySnapshot.getDocuments().get(0).toObject(RefreshToken.class);
            return Optional.ofNullable(rt);
        } catch (InterruptedException | ExecutionException e) {
            log.error("UserId로 Refresh Token 조회 실패", e);
            throw new RuntimeException("UserId로 Refresh Token 조회 중 오류 발생", e);
        }
    }

    public void delete(String token) {
        try {
            firestore.collection(COLLECTION_NAME)
                .document(token)
                .delete()
                .get();

            log.debug("Refresh Token 삭제 완료: token={}", token);
        } catch (InterruptedException | ExecutionException e) {
            log.error("Refresh Token 삭제 실패", e);
            throw new RuntimeException("Refresh Token 삭제 중 오류 발생", e);
        }
    }

    public void deleteByUserId(String userId) {
        try {
            var querySnapshot = firestore
                .collection(COLLECTION_NAME)
                .whereEqualTo("userId", userId)
                .get()
                .get();

            querySnapshot.getDocuments().forEach(doc -> {
                try {
                    doc.getReference().delete().get();
                } catch (InterruptedException | ExecutionException e) {
                    log.error("Refresh Token 삭제 실패: documentId={}", doc.getId(), e);
                }
            });

            log.debug("UserId의 모든 Refresh Token 삭제 완료: userId={}", userId);
        } catch (InterruptedException | ExecutionException e) {
            log.error("UserId로 Refresh Token 삭제 실패", e);
            throw new RuntimeException("UserId로 Refresh Token 삭제 중 오류 발생", e);
        }
    }

    public boolean existsByToken(String token) {
        return findByToken(token).isPresent();
    }
}
