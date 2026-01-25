package com.diract.global.common;

import com.google.cloud.Timestamp;

/**
 * 공통 엔티티 기본 클래스
 *
 * 모든 엔티티가 공통으로 가지는 필드 정의
 * - createdAt: 생성 시간
 * - updatedAt: 수정 시간
 * - deletedAt: 삭제 시간 (Soft Delete)
 */
public abstract class BaseEntity {

    protected Timestamp createdAt;
    protected Timestamp updatedAt;
    protected Timestamp deletedAt;

    public BaseEntity() {
        this.createdAt = Timestamp.now();
        this.updatedAt = Timestamp.now();
        this.deletedAt = null;
    }

    // Getters
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public Timestamp getDeletedAt() {
        return deletedAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setDeletedAt(Timestamp deletedAt) {
        this.deletedAt = deletedAt;
    }

    /**
     * 수정 시간 업데이트
     */
    public void updateTimestamp() {
        this.updatedAt = Timestamp.now();
    }

    /**
     * Soft Delete 처리
     */
    public void softDelete() {
        this.deletedAt = Timestamp.now();
        this.updatedAt = Timestamp.now();
    }

    /**
     * 삭제 여부 확인
     *
     * @return 삭제되었으면 true
     */
    public boolean isDeleted() {
        return this.deletedAt != null;
    }
}