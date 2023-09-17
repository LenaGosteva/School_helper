//package com.example.school.Adapters;
//
//import static com.example.school.App.authController;
//import static com.example.school.ui.dashboard.DashboardFragment.date;
//import static com.example.school.ui.dashboard.DashboardFragment.day;
//
//import android.app.Activity;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.school.App;
//import com.example.school.Logic.Subject;
//import com.example.school.R;
//import com.example.school.databinding.ActivityListBinding;
//import com.example.school.ui.dashboard.DashboardFragment;
//import com.google.firebase.database.DataSnapshot;
//
//import java.util.ArrayList;
//
//public class DashboardChooseSubjectAdapter extends RecyclerView.Adapter<DashboardChooseSubjectAdapter.SubjectViewHolder> {
//
//
//    private final Activity activity;
//
//    public ArrayList<Subject> getList() {
//        return list;
//    }
//
//    public void setList(ArrayList<Subject> list) {
//        this.list = list;
//    }
//
//    ArrayList<Subject> list;
//
//    public SubjectViewHolder getHolder_o() {
//        return holder_o;
//    }
//
//    SubjectViewHolder holder_o;
//
//
//    public DashboardChooseSubjectAdapter(ArrayList<Subject> list, Activity activity) {
//        this.list = list;
//        this.activity = activity;
//
//    }
//
//    @NonNull
//    @Override
//    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_for_choose_sb_in_dashboard, parent, false);
//
//        view.setLongClickable(true);
//        return new DashboardChooseSubjectAdapter.SubjectViewHolder(view);
//    }
//
//    @Override
//    public int getItemCount() {
//        return list.size();
//    }
//
//    public static class SubjectViewHolder extends RecyclerView.ViewHolder {
//        public TextView name;
//
//        public SubjectViewHolder(View itemView) {
//            super(itemView);
//            name = itemView.findViewById(R.id.name_of_subject_item_in_dashboard_choose);
//        }
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull DashboardChooseSubjectAdapter.SubjectViewHolder holder, int position) {
//        Subject subject = list.get(position);
//        Log.e("zssrdtfyug", subject.toString());
//holder_o = holder;
//        holder.name.setText(subject.getName());
//        holder.itemView.setBackgroundColor(activity.getColor(subject.getColor()));
//        holder.itemView.setOnClickListener(v -> {
//        });}
//
//}
