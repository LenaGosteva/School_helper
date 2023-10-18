package com.example.school.Auth;

import androidx.annotation.NonNull;

import com.example.school.Logic.Day;
import com.example.school.Logic.Subject;
import com.example.school.Logic.Task;
import com.example.school.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class AuthController {
    private final FirebaseAuth auth;

    FirebaseFirestore firebase;
    DatabaseReference database;

    public AuthController() {
        this.auth = FirebaseAuth.getInstance();
        this.database = FirebaseDatabase.getInstance("https://school-b6f87-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
    }

    public AuthController(FirebaseAuth auth) {
        this.auth = auth;
    }

    public boolean isAuth() {
        return auth.getCurrentUser() != null;
    }

    public FirebaseUser getUser() {
        return auth.getCurrentUser();
    }



    public void getAllDaysFromDb(OnCompleteListener<DataSnapshot> listener) {
        if (isAuth())
            database.child("users")
                    .child(getUser().getUid())
                    .child("days")
                    .get().addOnCompleteListener(listener);
    }
    public void getAllSubjectsFromDb(OnCompleteListener<DataSnapshot> listener) {
        if (isAuth())
            database.child("users")
                    .child(getUser().getUid())
                    .child("subjects")
                    .get().addOnCompleteListener(listener);
    }
    public void getAllTasksFromSubject(String subject, OnCompleteListener<DataSnapshot> listener) {
        if (isAuth()) {
            database.child("users")
                    .child(getUser().getUid())
                    .child("subjects")
                    .child(subject)
                    .child("tasks").get().addOnCompleteListener(listener);
        }
    }
    public void addUserToDb(OnCompleteListener listener) {
        if (isAuth()) {
            database.child("users").child(getUser().getUid()).setValue(getUser().getUid()).addOnCompleteListener(listener);
            addDayToDb(new Day(), listener);
            addSubjectToDb(new Subject("Additionally", "Some additional things that are not related to the lessons", R.color.sb_purple), listener);

        }
    }
    public void getDayFromDB(String date,OnCompleteListener<DataSnapshot> listener) {
        if (isAuth())
            database.child("users").child(getUser().getUid()).child("days").child(date).get().addOnCompleteListener(listener);
    }

    public void addDayToDb(Day day, OnCompleteListener listener) {
        if (isAuth()) {
            database.child("users").child(getUser().getUid()).child("days").child(day.getDate().toString()).setValue(day).addOnCompleteListener(listener);
        }
    }
    public void addSubjectToDb(Subject subject, OnCompleteListener listener) {
        if (isAuth()) {
            database.child("users").child(getUser().getUid()).child("subjects").child(subject.getName()).setValue(subject).addOnCompleteListener(listener);
        }
    }

    public void addSubjectToDay(String subject,String index, String day, OnCompleteListener listener) {
        if (isAuth()) {
            database.child("users").child(getUser().getUid()).child("days").child(day).child("subjects").child(index).setValue(subject).addOnCompleteListener(listener);
        }
    }
    public void addTaskToDay(String task,String index, String day, OnCompleteListener listener) {
        if (isAuth()) {
            database.child("users").child(getUser().getUid()).child("days").child(day).child("tasks").child(index).setValue(task).addOnCompleteListener(listener);
        }
    }
    public void getSubjectsFromDay(String day, OnCompleteListener<DataSnapshot> listener) {
        if (isAuth())
            database.child("users").child(getUser().getUid()).child("days").child(day).child("subjects").get().addOnCompleteListener(listener);
    }
    public void getTasksFromDay(String day, OnCompleteListener<DataSnapshot> listener) {
        if (isAuth())
            database.child("users").child(getUser().getUid()).child("days").child(day).child("tasks").get().addOnCompleteListener(listener);
    }
    public void removeSubjectsFromDay(String day, OnCompleteListener listener) {
        if (isAuth())
            database.child("users").child(getUser().getUid()).child("days").child(day).child("subjects").removeValue().addOnCompleteListener(listener);
    }
    public void removeTasksFromDay(String day, OnCompleteListener listener) {
        if (isAuth())
            database.child("users").child(getUser().getUid()).child("days").child(day).child("tasks").removeValue().addOnCompleteListener(listener);
    }

    public void getSubjectFromDB(String subject,OnCompleteListener<DataSnapshot> listener) {
        if (isAuth())
            database.child("users").child(getUser().getUid()).child("subjects").child(subject).get().addOnCompleteListener(listener);
    }
    public void removeSubjectFromDB(String subject,OnCompleteListener listener) {
        if (isAuth())
            database.child("users").child(getUser().getUid()).child("subjects").child(subject).removeValue().addOnCompleteListener(listener);
    }
    public void addTaskToSubject(Task task, String index, String subject, OnCompleteListener listener) {
        if (isAuth()) {
            database.child("users").child(getUser().getUid()).child("subjects").child(subject).child("tasks").child(index).setValue(task).addOnCompleteListener(listener);
        }
    }public void addCheckedToTask(boolean checked, String index, String subject, OnCompleteListener listener) {
        if (isAuth()) {
            database.child("users").child(getUser().getUid()).child("subjects").child(subject).child("tasks").child(index).child("completed").setValue(checked).addOnCompleteListener(listener);
        }
    }
    public void getTaskFromSubject(String index, String subject, OnCompleteListener<DataSnapshot> listener) {
        if (isAuth()) {
            database.child("users").child(getUser().getUid()).child("subjects").child(subject).child("tasks").child(index).get().addOnCompleteListener(listener);
        }
    }

    public void removeTaskFromSubject(String task, String sb, OnCompleteListener listener){
        if (isAuth()){
            database.child("users").child(getUser().getUid()).child("subjects").child(sb).child("tasks").child(task).removeValue().addOnCompleteListener(listener);
        }
    }

    public void registerUser(String email, String password, OnCompleteListener listener) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(listener);


    }


    public void updateName(String name) {
        if (isAuth()) {
            getUser().updateProfile(new UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build()
            );
            database.child("users").child(getUser().getUid()).child("nickname").setValue(name);

        }


    }

    public void sendMailWithNewPassword(String email, OnCompleteListener<Void> listener) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.sendPasswordResetEmail(email).addOnCompleteListener(listener);
    }

    public void enterUser(String email, String password, OnCompleteListener<AuthResult> listener) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(listener);
    }

    public void singOut() {
        auth.signOut();
    }


    public FirebaseAuth getAuth() {
        return auth;
    }

    public FirebaseFirestore getFirebase() {
        return firebase;
    }

    public void setFirebase(FirebaseFirestore firebase) {
        this.firebase = firebase;
    }

    public DatabaseReference getDatabase() {
        return database;
    }

    public void setDatabase(DatabaseReference database) {
        this.database = database;
    }
}
