package com.diract.global.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
@Slf4j
public class FirebaseConfig {

    @Value("${firebase.project-id:dancemachine-5943b}")
    private String projectId;

    @Value("${firebase.emulator.enabled:false}")
    private boolean emulatorEnabled;

    @Value("${firebase.emulator.firestore-host:localhost:8081}")
    private String emulatorHost;

    @Value("${firebase.credentials.path:}")
    private String credentialsPath;

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        FirebaseOptions.Builder builder = FirebaseOptions.builder()
            .setProjectId(projectId);

        // firebase-admin 9.2.0은 credentials 없으면 build()에서 NPE 남
        // 그래서 emulator에서도 "형식상" credentials를 넣어줘야 함
        GoogleCredentials credentials;

        if (!emulatorEnabled) {
            // dev/prod: 서비스계정키 또는 ADC
            if (credentialsPath != null && !credentialsPath.isBlank()) {
                try (InputStream in = new FileInputStream(credentialsPath)) {
                    credentials = GoogleCredentials.fromStream(in);
                }
            } else {
                credentials = GoogleCredentials.getApplicationDefault();
            }
        } else {
            // local emulator: 더미 credentials
            credentials = GoogleCredentials.create(null);
        }

        builder.setCredentials(credentials);

        FirebaseOptions options = builder.build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }

        log.info("Firebase initialized. projectId={}, emulator={}", projectId, emulatorEnabled);
        return FirebaseApp.getInstance();
    }

    @Bean
    public Firestore firestore(FirebaseApp firebaseApp) {
        if (emulatorEnabled) {
            // 여기서 에뮬레이터로 강제 연결되므로 실프로젝트로 안 나감
            log.info("Using Firestore Emulator: {}", emulatorHost);

            return FirestoreOptions.newBuilder()
                .setProjectId(projectId)
                .setHost(emulatorHost)
                .setCredentials(GoogleCredentials.create(null))
                .build()
                .getService();
        }

        log.info("Using Firestore Production");
        return FirestoreClient.getFirestore(firebaseApp);
    }
}
