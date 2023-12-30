package com.example.test;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class MainActivity3 extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        recyclerView = findViewById(R.id.listview);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();
            }
        });

        fetchData();

        ImageView btnKembali = findViewById(R.id.kembali1);
        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity3.this, MainActivity2.class);
                startActivity(intent);
            }
        });
    }

    private void fetchData() {
        String url = "https://reqres.in/api/users?page=2";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    List<UserModel> userList = new ArrayList<>();
                    JSONArray users = response.getJSONArray("data");

                    for (int i = 0; i < users.length(); i++) {
                        JSONObject user = users.getJSONObject(i);

                        String email = user.getString("email");
                        String firstname = user.getString("first_name");
                        String lastname = user.getString("last_name");
                        String avatarUrl = user.getString("avatar");

                        userList.add(new UserModel(email, firstname, lastname, avatarUrl));
                    }

                    UserAdapter adapter = new UserAdapter(userList);
                    recyclerView.setAdapter(adapter);

                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        Volley.newRequestQueue(this).add(request);
    }

    public class UserModel {
        private String email;
        private String firstname;
        private String lastname;
        private String avatar;

        public UserModel(String email, String firstname, String lastname, String avatar) {
            this.email = email;
            this.firstname = firstname;
            this.lastname = lastname;
            this.avatar = avatar;
        }

        public String getEmail() {
            return email;
        }
        public String getFirstname() {
            return firstname;
        }
        public String getLastname() {
            return lastname;
        }
        public String getAvatar() {
            return avatar;
        }
    }

    public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
        private List<UserModel> userList;
        public UserAdapter(List<UserModel> userList) {
            this.userList = userList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.isianlist, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            UserModel user = userList.get(position);
            holder.firstnameTextView.setText(user.getFirstname());
            holder.lastnameTextView.setText(user.getLastname());
            holder.emailTextView.setText(user.getEmail());
            Glide.with(holder.itemView.getContext())
                    .load(user.getAvatar())
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(holder.avatarImageView);
        }

        @Override
        public int getItemCount() {
            return userList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView avatarImageView;
            TextView firstnameTextView;
            TextView lastnameTextView;
            TextView emailTextView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                avatarImageView = itemView.findViewById(R.id.gambar_avatar);
                firstnameTextView = itemView.findViewById(R.id.firstname);
                lastnameTextView = itemView.findViewById(R.id.lastname);
                emailTextView = itemView.findViewById(R.id.email);
            }
        }
    }
}
