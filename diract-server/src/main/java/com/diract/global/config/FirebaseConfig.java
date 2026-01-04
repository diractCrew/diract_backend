package com.diract.global.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

/**
 * Firebase Admin SDK 초기화 설정
 * - local 프로필: Firestore Emulator 사용 (localhost:8081)
 * - dev/prod 프로필: 실제 Firebase 연결
 */
@Slf4j
@Configuration
public class FirebaseConfig {
    public FirebaseConfig() {
        log.info("FirebaseConfig 생성자 호출됨!");
    }

    @Value("${firebase.emulator.enabled:false}")
    private boolean emulatorEnabled;

    /**
     * Firebase 초기화 - resources/firebase-key.json 파일로 인증 - Emulator 모드 활성화 시 로컬 Firestore 연결
     */
    @PostConstruct
    public void initialize() throws IOException {
        try {
            if (emulatorEnabled) {
                System.setProperty("FIRESTORE_EMULATOR_HOST", "localhost:8081");
                log.info("Firestore Emulator 모드 활성화 - localhost:8081");
            }

            // Firebase 서비스 계정 키 로드
            InputStream serviceAccount = new ClassPathResource(
                "firebase-key.json").getInputStream();

            FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

            // Firebase 앱 초기화
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                log.info("Firebase 초기화 성공");

                if (!emulatorEnabled) {
                    log.info("실제 Firebase 프로젝트 연결");
                }
            }

        } catch (IOException e) {
            log.error("Firebase 초기화 실패: {}", e.getMessage());
            throw e;
        }
    }
}