package com.example.designapptest.view.room;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.designapptest.R;

public class ReportRoomDialogFragment extends AppCompatDialogFragment implements View.OnClickListener {
    EditText edtDetailedReasonReportRoom;
    RadioButton radReason1ReportRoomDialog;
    RadioButton radReason2ReportRoomDialog;
    RadioButton radReason3ReportRoomDialog;
    ReportRoomDialogListener reportRoomDialogListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.PauseDialog);

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.report_room_dialog, null);

        builder.setView(view)
                .setTitle("Báo cáo")
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Gửi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String reasonReportRoom = "";
                        String detailedReasonReportRoom = edtDetailedReasonReportRoom.getText().toString();

                        if (radReason1ReportRoomDialog.isChecked()) {
                            reasonReportRoom += radReason1ReportRoomDialog.getText();
                        } else if (radReason2ReportRoomDialog.isChecked()) {
                            reasonReportRoom += radReason2ReportRoomDialog.getText();
                        } else {
                            reasonReportRoom += radReason3ReportRoomDialog.getText();
                        }

                        reportRoomDialogListener.applyText(reasonReportRoom, detailedReasonReportRoom);
                    }
                });

        edtDetailedReasonReportRoom = (EditText) view.findViewById(R.id.edt_detailed_reason_report_room_dialog);
        radReason1ReportRoomDialog = (RadioButton) view.findViewById(R.id.rad_reason_1_report_room_dialog);
        radReason2ReportRoomDialog = (RadioButton) view.findViewById(R.id.rad_reason_2_report_room_dialog);
        radReason3ReportRoomDialog = (RadioButton) view.findViewById(R.id.rad_reason_3_report_room_dialog);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            reportRoomDialogListener = (ReportRoomDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement ReportRoomDialogListener");
        }

    }

    public interface ReportRoomDialogListener {
        void applyText(String reasonReportRoom, String detailedReasonReportRoom);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.rad_reason_1_report_room_dialog:
                radReason2ReportRoomDialog.setChecked(false);
                radReason3ReportRoomDialog.setChecked(false);
                break;
            case R.id.rad_reason_2_report_room_dialog:
                radReason1ReportRoomDialog.setChecked(false);
                radReason3ReportRoomDialog.setChecked(false);
                break;
            case R.id.rad_reason_3_report_room_dialog:
                radReason1ReportRoomDialog.setChecked(false);
                radReason2ReportRoomDialog.setChecked(false);
                break;
        }
    }
}
