package EcomerceApp.ShoppingApp;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import EcomerceApp.ShoppingApp.databinding.ActivityUserAnalyticsBinding;

public class UserAnalyticsActivity extends AppCompatActivity {

    private static final String TAG = "UserAnalyticsActivity";
    private ActivityUserAnalyticsBinding binding;
    private FirebaseFirestore db;
    private ProgressBar progressBar;
    private TextView tvNoData;
    private BarChart barChart;
    private PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserAnalyticsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize views
        progressBar = binding.progressBar;
        tvNoData = binding.tvNoData;
        barChart = binding.barChart;
        pieChart = binding.pieChart;

        // Set title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("User Analytics");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Load user analytics data
        loadUserAnalytics();
    }

    private void loadUserAnalytics() {
        progressBar.setVisibility(View.VISIBLE);
        tvNoData.setVisibility(View.GONE);

        Log.d(TAG, "Loading user analytics data...");

        db.collection("users")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    progressBar.setVisibility(View.GONE);

                    if (queryDocumentSnapshots.isEmpty()) {
                        Log.d(TAG, "No user data available for analytics");
                        tvNoData.setVisibility(View.VISIBLE);
                        return;
                    }

                    // Process user data for graphs
                    List<String> userEmails = new ArrayList<>();
                    Map<String, Integer> domainCounts = new HashMap<>();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String email = document.getString("CUSTOMER_EMAIL");
                        if (email != null && !email.isEmpty()) {
                            userEmails.add(email);

                            // Extract domain for pie chart
                            String domain = email.substring(email.indexOf("@") + 1);
                            domainCounts.put(domain, domainCounts.getOrDefault(domain, 0) + 1);
                        }
                    }

                    // If we have user data, create graphs
                    if (!userEmails.isEmpty()) {
                        createRegistrationTrendChart();
                        createEmailDomainPieChart(domainCounts);
                    } else {
                        tvNoData.setVisibility(View.VISIBLE);
                    }

                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    tvNoData.setVisibility(View.VISIBLE);
                    Log.e(TAG, "Error loading user data for analytics", e);
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void createRegistrationTrendChart() {
        // For demo purposes, we'll create simulated registration data
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        // Get the last 6 months for the chart
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM", Locale.getDefault());

        for (int i = 5; i >= 0; i--) {
            calendar.add(Calendar.MONTH, -1);
            String monthName = sdf.format(calendar.getTime());
            labels.add(monthName);

            // Simulate user registration count
            float value = (float) (Math.random() * 10 + 1);
            entries.add(new BarEntry(5-i, value));
        }

        // Reverse the order so most recent month is on the right
        Collections.reverse(labels);

        BarDataSet dataSet = new BarDataSet(entries, "User Registrations");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(12f);

        BarData barData = new BarData(dataSet);
        barChart.setData(barData);

        // Customize X axis
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);

        // Other customizations
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(true);
        barChart.setDrawGridBackground(false);
        barChart.setDrawBarShadow(false);
        barChart.setHighlightFullBarEnabled(false);

        // Animate and refresh
        barChart.animateY(1000);
        barChart.invalidate();
    }

    private void createEmailDomainPieChart(Map<String, Integer> domainCounts) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();

        // Get the top 5 domains by count
        List<Map.Entry<String, Integer>> sortedDomains = new ArrayList<>(domainCounts.entrySet());
        sortedDomains.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        int otherCount = 0;
        for (int i = 0; i < sortedDomains.size(); i++) {
            Map.Entry<String, Integer> entry = sortedDomains.get(i);
            if (i < 4) { // Top 4 domains
                entries.add(new PieEntry(entry.getValue(), entry.getKey()));
                colors.add(ColorTemplate.VORDIPLOM_COLORS[i % ColorTemplate.VORDIPLOM_COLORS.length]);
            } else { // Combine the rest as "Other"
                otherCount += entry.getValue();
            }
        }

        // Add the "Other" category if there are additional domains
        if (otherCount > 0) {
            entries.add(new PieEntry(otherCount, "Other"));
            colors.add(Color.GRAY);
        }

        PieDataSet dataSet = new PieDataSet(entries, "Email Domains");
        dataSet.setColors(colors);
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.WHITE);

        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);

        // Customize pie chart
        pieChart.getDescription().setEnabled(false);
        pieChart.setUsePercentValues(true);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(30f);
        pieChart.setHoleRadius(30f);
        pieChart.setDrawEntryLabels(true);
        pieChart.setEntryLabelTextSize(12f);

        // Animate and refresh
        pieChart.animateY(1000);
        pieChart.invalidate();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}