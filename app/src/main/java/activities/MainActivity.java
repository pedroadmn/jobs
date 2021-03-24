package activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapters.CompanyAdapter;
import listeners.RecyclerItemClickListener;
import models.Company;
import pedroadmn.com.companyjobs.R;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvCompanies;
    private List<Company> companies = new ArrayList<>();
    private CompanyAdapter companyAdapter;
    private MaterialSearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Jobs");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        rvCompanies = findViewById(R.id.rvCompanies);
        searchView = findViewById(R.id.search_view);

        getCompanies();

        searchView.setHint("Search companies");

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchCompanies(newText);
                return true;
            }
        });
    }

    public void searchCompanies(String searchText) {
        List<Company> searchCompanyList = new ArrayList<>();

        for (Company company : companies) {
            String companyName = company.getName().toLowerCase();

            if (companyName.contains(searchText)) {
                searchCompanyList.add(company);
            }
        }

        companyAdapter = new CompanyAdapter(this, searchCompanyList);
        rvCompanies.setAdapter(companyAdapter);
        companyAdapter.notifyDataSetChanged();
    }

    private void getCompanies() {
        try {
            JSONObject obj = new JSONObject(readJSONFromAsset());
            try {
                JSONArray m_jArry = obj.getJSONArray("companies");
                ArrayList<HashMap<String, String>> formList = new ArrayList<HashMap<String, String>>();
                HashMap<String, String> m_li;

                for (int i = 0; i < m_jArry.length(); i++) {
                    JSONObject jo_inside = m_jArry.getJSONObject(i);
                    String name = jo_inside.getString("companyName");
                    String url_value = jo_inside.getString("url");
                    String cover = jo_inside.getString("cover");

                    Company company = new Company();
                    company.setName(name);
                    company.setUrl(url_value);
                    company.setCover(cover);

                    companies.add(company);
                }

                recyclerViewConfig();

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("JSON", "onCreate: " + obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String readJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("companies.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private void recyclerViewConfig() {
        rvCompanies.setHasFixedSize(true);
        rvCompanies.setLayoutManager(new LinearLayoutManager(this));
        companyAdapter = new CompanyAdapter(this, companies);
        rvCompanies.setAdapter(companyAdapter);

        rvCompanies.addOnItemTouchListener(new RecyclerItemClickListener(this, rvCompanies,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Company company = companies.get(position);
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(company.getUrl()));
                        startActivity(browserIntent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }
                }));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.searchMenu);
        searchView.setMenuItem(searchItem);

        return super.onCreateOptionsMenu(menu);
    }
}