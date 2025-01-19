package com.ireddragonicy.nadma;

import android.content.Context;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class SessionManager {

    private final GoogleSignInClient googleSignInClient;
    private final Context context;

    public SessionManager(Context context) {
        this.context = context;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build();
        googleSignInClient = GoogleSignIn.getClient(context, gso);
    }

    public boolean isLoggedIn() {
        return GoogleSignIn.getLastSignedInAccount(context) != null;
    }

    public GoogleSignInAccount getSignedInAccount() {
        return GoogleSignIn.getLastSignedInAccount(context);
    }

    public void signOut(Runnable onSignedOut) {
        googleSignInClient.signOut()
            .addOnCompleteListener(task -> onSignedOut.run());
    }
}