package phone.vishnu.breathe_doitright;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

public class EditFragment extends Fragment {

    private SeekBar timeUp;
    private TextView progressTv,resetTv;

    public EditFragment() {
        // Required empty public constructor
    }

    public static EditFragment newInstance() {
        return new EditFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit, container, false);
        timeUp = view.findViewById(R.id.editTimeSeekBar);
        progressTv = view.findViewById(R.id.progressTv);
        resetTv= view.findViewById(R.id.resetTv);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        timeUp.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                MainActivity.DURATION = progress;
                progressTv.setText(String.format("%ss", String.valueOf(progress / 1000)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        resetTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.DURATION=5000;
                timeUp.setProgress(5000);
                progressTv.setText(String.format("%ss", String.valueOf(5)));
            }
        });

    }
}
