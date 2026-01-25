package com.diract.domain.user.repository;

import com.diract.domain.user.entity.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * Firestore users 컬렉션 접근 레포지토리
 *
 * - User 저장 및 조회 담당
 * - userId(documentId), email 기준 조회 제공
 * - 삭제는 Soft Delete 정책만 사용
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRepository {

    private static final String COLLECTION_NAME = "users";

    private final Firestore firestore;

    /** User 저장 또는 수정 */
    public User save(User user) {
        try {
            ApiFuture<WriteResult> future = firestore
                .collection(COLLECTION_NAME)
                .document(user.getUserId())
                .set(user);

            future.get();
            log.debug("User 저장 완료: userId={}", user.getUserId());

            return user;

        } catch (InterruptedException | ExecutionException e) {
            log.error("User 저장 실패", e);
            throw new RuntimeException("User 저장 중 오류 발생", e);
        }
    }

    /** userId로 User 조회 */
    public Optional<User> findById(String userId) {
        try {
            ApiFuture<DocumentSnapshot> future = firestore
                .collection(COLLECTION_NAME)
                .document(userId)
                .get();

            DocumentSnapshot document = future.get();

            if (document.exists()) {
                return Optional.ofNullable(document.toObject(User.class));
            }

            return Optional.empty();

        } catch (InterruptedException | ExecutionException e) {
            log.error("User 조회 실패: userId={}", userId, e);
            throw new RuntimeException("User 조회 중 오류 발생", e);
        }
    }

    /** 이메일로 User 조회 */
    public Optional<User> findByEmail(String email) {
        try {
            ApiFuture<QuerySnapshot> future = firestore
                .collection(COLLECTION_NAME)
                .whereEqualTo("email", email)
                .limit(1)
                .get();

            QuerySnapshot querySnapshot = future.get();

            if (!querySnapshot.isEmpty()) {
                return Optional.ofNullable(
                    querySnapshot.getDocuments().get(0).toObject(User.class)
                );
            }

            return Optional.empty();

        } catch (InterruptedException | ExecutionException e) {
            log.error("이메일로 User 조회 실패: email={}", email, e);
            throw new RuntimeException("이메일로 User 조회 중 오류 발생", e);
        }
    }

    /** userId 존재 여부 확인 */
    public boolean existsById(String userId) {
        return findById(userId).isPresent();
    }

    /** 이메일 존재 여부 확인 */
    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }
}
