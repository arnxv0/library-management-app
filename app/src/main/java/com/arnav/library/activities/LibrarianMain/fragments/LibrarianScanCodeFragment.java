package com.arnav.library.activities.LibrarianMain.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arnav.library.R;
import com.arnav.library.activities.LibrarianMain.fragments.FragmentActionListener;
import com.arnav.library.databinding.FragmentLibrarianScanCodeBinding;
import com.arnav.library.models.Librarian;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

public class LibrarianScanCodeFragment extends Fragment {

    FragmentLibrarianScanCodeBinding binding;
    private CodeScanner codeScanner;
    FragmentActionListener fragmentActionListener;

    public LibrarianScanCodeFragment() {
        // Required empty public constructor
    }

    public static LibrarianScanCodeFragment newInstance(
            Librarian librarian,
            FragmentActionListener fragmentActionListener
    ) {
        LibrarianScanCodeFragment fragment = new LibrarianScanCodeFragment();
        Bundle args = new Bundle();
        args.putBundle("librarian", librarian.getBundle());
        fragment.setArguments(args);
        fragment.setFragmentActionListener(fragmentActionListener);
        return fragment;
    }

    public void setFragmentActionListener(FragmentActionListener fragmentActionListener) {
        this.fragmentActionListener = fragmentActionListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLibrarianScanCodeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Activity activity = getActivity();
        codeScanner = new CodeScanner(activity, binding.codeScannerView);
        codeScanner.setDecodeCallback(
                result -> activity.runOnUiThread(
                        () -> getResult(result.getText())
                )
        );
        binding.codeScannerView.setOnClickListener(view1 -> codeScanner.startPreview());
    }

    void getResult(String info) {
        Log.i("code", info);
        if (fragmentActionListener != null) {
            Bundle bundle = new Bundle();
            bundle.putInt(
                    FragmentActionListener.ACTION_KEY,
                    FragmentActionListener.SCAN_CODE_ACTION_VALUE
            );
            bundle.putString(
                    FragmentActionListener.SCAN_CODE_KEY,
                    info
            );
            fragmentActionListener.onActionPerformed(bundle);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }

    @Override
    public void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }
}