package EcomerceApp.ShoppingApp;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
//Class AboutFragment for About section
public class AboutFragment extends Fragment {
    //Empty constructor
    public AboutFragment() {

    }
    //On create view, load xml fragment_about layout
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_about, container, false);
    }
}
