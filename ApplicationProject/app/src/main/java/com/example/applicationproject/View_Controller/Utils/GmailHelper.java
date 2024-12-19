//package com.example.applicationproject.View_Controller.Utils;
//
//import static com.google.api.services.gmail.GmailScopes.GMAIL_READONLY;
//
//import android.accounts.Account;
//import android.app.Activity;
//import android.content.Intent;
//
//import com.google.android.gms.auth.api.signin.GoogleSignIn;
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//import com.google.android.gms.auth.api.signin.GoogleSignInClient;
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
//import com.google.android.gms.common.api.Scope;
//import com.google.api.client.auth.oauth2.Credential;
//import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
//import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
//import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
//import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
//import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
//import com.google.api.client.http.javanet.NetHttpTransport;
//import com.google.api.client.json.gson.GsonFactory;
//import com.google.api.client.util.store.FileDataStoreFactory;
//import com.google.api.services.gmail.Gmail;
//import com.google.api.services.gmail.GmailScopes;
//import com.google.api.services.gmail.model.ListMessagesResponse;
//import com.google.api.services.gmail.model.Message;
//
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.nio.file.Paths;
//import java.security.GeneralSecurityException;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//import java.util.Objects;
//
//public class GmailHelper {
//
//    private static final String APPLICATION_NAME = "ToDoList";
//    private static final int REQUEST_SIGN_IN = 1001;
//
//    private final GoogleSignInClient googleSignInClient;
//
//    public GmailHelper(Activity activity) {
//        // Khởi tạo GoogleSignInOptions với quyền truy cập Gmail
//        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .requestScopes(new Scope(GmailScopes.GMAIL_READONLY))  // Quyền truy cập Gmail
//                .build();
//
//        // Khởi tạo GoogleSignInClient để xử lý quá trình đăng nhập
//        googleSignInClient = GoogleSignIn.getClient(activity, signInOptions);
//    }
//
//    // Phương thức yêu cầu người dùng đăng nhập hoặc lấy tài khoản nếu đã đăng nhập
//    public void signIn(Activity activity) {
//        Intent signInIntent = googleSignInClient.getSignInIntent();
//        activity.startActivityForResult(signInIntent, REQUEST_SIGN_IN);
//    }
//
//    // Phương thức xử lý kết quả đăng nhập từ onActivityResult
//    public void handleSignInResult(Intent data, Activity activity) {
//        GoogleSignInAccount account = GoogleSignIn.getSignedInAccountFromIntent(data).getResult();
//
//        if (account != null) {
//            try {
//                // Sau khi xác thực thành công, xây dựng Gmail service
//                final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
//                Gmail service = new Gmail.Builder(HTTP_TRANSPORT, GsonFactory.getDefaultInstance(), getCredentials(GoogleSignInAccount.fromAccount(Objects.requireNonNull(account.getAccount()))))
//                        .setApplicationName(APPLICATION_NAME)
//                        .build();
//
//                // Gọi phương thức listEmails để lấy danh sách email
//                listEmails(service);
//
//            } catch (IOException | GeneralSecurityException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    // Phương thức lấy Credential từ tài khoản Google
//    private static Credential getCredentials(GoogleSignInAccount account) throws IOException, GeneralSecurityException {
//        // Tạo đối tượng NetHttpTransport
//        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
//
//        // Tải thông tin client secrets từ file json
//        GoogleClientSecrets clientSecrets =
//                GoogleClientSecrets.load(GsonFactory.getDefaultInstance(),
//                        new InputStreamReader(GmailHelper.class.getResourceAsStream("/client_secret_885749277570-67hh8i9fuib75hius3is42m5vgila4iq.apps.googleusercontent.com.json")));
//
//        // Tạo GoogleAuthorizationCodeFlow
//        GoogleAuthorizationCodeFlow flow = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            flow = new GoogleAuthorizationCodeFlow.Builder(
//                    HTTP_TRANSPORT,
//                    GsonFactory.getDefaultInstance(),
//                    clientSecrets,
//                    Collections.singletonList(GMAIL_READONLY))
//                    .setDataStoreFactory(new FileDataStoreFactory(Paths.get("tokens").toFile()))
//                    .setAccessType("offline")
//                    .build();
//        }
//
//        // Tạo đối tượng LocalServerReceiver (dùng cho việc cài đặt server để lấy mã xác thực)
//        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
//
//        // Sử dụng tài khoản đã đăng nhập để lấy Credential
//        assert flow != null;
//        return new AuthorizationCodeInstalledApp(flow, receiver).authorize(account.getEmail());
//    }
//
//
//    // Phương thức để lấy danh sách email từ Gmail
//    public void listEmails(Gmail service) {
//        try {
//            // Lấy danh sách các email trong hộp thư đến (INBOX)
//            ListMessagesResponse messagesResponse = service.users().messages().list("me")
//                    .setLabelIds(Collections.singletonList("INBOX"))
//                    .execute();
//
//            List<Message> messages = messagesResponse.getMessages();
//            if (messages != null) {
//                for (Message message : messages) {
//                    // Lấy thông tin chi tiết email
//                    Message fullMessage = service.users().messages().get("me", message.getId()).execute();
//                    System.out.println("Email subject: " + fullMessage.getSnippet());
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    // Hàm gọi khi bạn muốn hủy đăng nhập
//    public void signOut() {
//        googleSignInClient.signOut();
//    }
//}
