package com.stardash.sportdash.plans.run.inspect;

import static com.stardash.sportdash.plans.run.RunPlanActivity.thePlan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.stardash.sportdash.R;
import com.stardash.sportdash.plans.create.structure.CreateStructureNewActivity;
import com.stardash.sportdash.plans.create.structure.StructureAdapter;
import com.stardash.sportdash.plans.create.structure.StructureItem;
import com.stardash.sportdash.settings.Account;

import java.util.ArrayList;

public class InspectActivity extends AppCompatActivity {

    int elementsInPlan = -1;
    public static Boolean inspectingPlan = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspect);
        if (Account.isAmoled()) {
            ConstraintLayout main = findViewById(R.id.main);
            main.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.BLACK);
            }
        }
        buildPage();
    }

    private void buildPage() {
        String items = "";
        for (int i=1; i < 100; i++) {

            try {
                String add = planNames(i);
                String format = planFormat(i);
                String seconds = planSeconds(i);

                if (add.equals("nOnE")) {

                } else if (add.equals("S Ξ L Ξ C T")) {

                } else if (add.contains("C H O O S E")) {

                } else if (add.contains("S Ξ L Ξ C T")) {

                } else {
                    items += i-1 + ".@" + add + "@" + seconds + " " + format + "\n";
                    elementsInPlan++;
                }
            } catch (Exception e){

            }

        }

        createList(items);
    }

    private ArrayList<StructureItem> mStructureList;
    private RecyclerView mRecyclerView;
    private StructureAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public void createList(String list){
        String[] item = list.split("\n");

        mStructureList = new ArrayList<>();

        for (String element : item) {
            try {
                mStructureList.add(new StructureItem(element.split("@")[0], element.split("@")[1], element.split("@")[2]));
            } catch (Exception e) {

            }
        }

        buildRecyclerView();
    }

    public void buildRecyclerView(){
        mRecyclerView = findViewById(R.id.structure_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(InspectActivity.this);
        mAdapter = new StructureAdapter(mStructureList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }


    private String planNames(int element) {
        String elementNames = thePlan().split("\n",20)[13];
        String selectedElement = elementNames.split("&",25)[element];
        return selectedElement;
    }


    private String planSeconds(int element) {
        String elementNames = thePlan().split("\n",20)[16];
        String selectedElement = elementNames.split("&",25)[element];
        return selectedElement;
    }

    private String planFormat(int element) {
        String elementNames = thePlan().split("\n",20)[17];
        String selectedElement = elementNames.split("&",25)[element];
        return selectedElement;
    }


}