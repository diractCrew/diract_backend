package com.diract.global.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

/**
 * Firebase Admin SDK 초기화 설정
 *
 * - Firestore 사용을 위한 FirebaseApp 초기화
 * - 로컬 환경에서는 Firestore Emulator 사용
 * - 그 외 환경에서는 실제 Firebase 프로젝트 연결
 */
@Slf4j
@Configuration
public class FirebaseConfig {

    @Value("${firebase.emulator.enabled:false}")
    private boolean emulatorEnabled;

    /**
     * Firebase 초기화
     *
     * - Emulator 사용 시 FIRESTORE_EMULATOR_HOST 설정
     * - 서비스 계정 키(firebase-key.json)를 이용해 FirebaseApp 초기화
     */
    @PostConstruct
    public void initialize() throws IOException {
        try {
            if (emulatorEnabled) {
                System.setProperty("FIRESTORE_EMULATOR_HOST", "localhost:8081");
                log.info("Firestore Emulator enabled (localhost:8081)");
            }

            // Firebase 서비스 계정 키 로드
            InputStream serviceAccount =
                new ClassPathResource("firebase-key.json").getInputStream();

            FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

            // FirebaseApp은 JVM 당 한 번만 초기화
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                log.info("Firebase initialized (emulatorEnabled={})", emulatorEnabled);
            }

        } catch (IOException e) {
            log.error("Firebase initialization failed", e);
            throw e;
        }
    }

    /**
     * Firestore Bean 등록
     */
    @Bean
    public Firestore firestore() {
        return FirestoreClient.getFirestore();
    }
}
