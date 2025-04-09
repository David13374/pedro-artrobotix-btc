package opmodes;

import android.graphics.Color;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import classes.ColorDetection;
import classes.Intake;

@TeleOp(group = "TeleOP", name = "MainOpMode")
public class MainOpMode extends LinearOpMode {
    Intake intake;
    @Override
    public void runOpMode() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        ColorDetection.Team team = ColorDetection.Team.RED;
        GamepadEx gamepad1Ex;
        gamepad1Ex = new GamepadEx(gamepad1);
        intake = new Intake(hardwareMap, team);
        while (opModeInInit()) {
            if (gamepad1Ex.wasJustPressed(GamepadKeys.Button.X)) {
                intake.updateTeam(ColorDetection.Team.BLUE);
                team = ColorDetection.Team.BLUE;
            }
            if (gamepad1Ex.wasJustPressed(GamepadKeys.Button.Y)) {
                intake.updateTeam(ColorDetection.Team.RED);
                team = ColorDetection.Team.RED;
            }

            gamepad1Ex.readButtons();
            telemetry.addData("Distance", intake.getDistance());
            telemetry.addData("Team", team);
            telemetry.addData("Red", intake.getRed());
            telemetry.addData("Blue", intake.getBlue());
            telemetry.addData("Green", intake.getGreen());
            telemetry.update();
        }
        waitForStart();

        while (opModeIsActive()) {
            intake.update();
            telemetry.addData("Distance", intake.getDistance());
            telemetry.addData("Team", team);
            telemetry.addData("Red", intake.getRed());
            telemetry.addData("Blue", intake.getBlue());
            telemetry.addData("Green", intake.getGreen());
            telemetry.addData("State", intake.currentState);
            telemetry.update();
        }
    }
}