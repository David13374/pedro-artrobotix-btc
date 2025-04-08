package classes;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

@Config
public class Intake {
    DcMotorEx motor;
    ColorDetection colorDetect;

    enum State {
        OUT,
        IN,
        STOP
    }

    State currentState;

    public static double inPower = -1, outPower = 0.75;

    ElapsedTime timer;
    public double getDistance() {
        return colorDetect.getDistance();
    }

    public double getRed() {
        return colorDetect.getRed();
    }
    public double getBlue() {
        return colorDetect.getBlue();
    }

    public void updateTeam(ColorDetection.Team team) {
        colorDetect.updateTeam(team);
    }
    public Intake(HardwareMap hmap, ColorDetection.Team team) {
        motor = hmap.get(DcMotorEx.class, "Intake");
        colorDetect = new ColorDetection(hmap, team);
        timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        timer.startTime();
    }
    public static double outTime = 300;
    boolean reset = false;
    public void update() {
        if(timer.milliseconds() > outTime) {
            if (reset)
                reset = false;
            currentState = colorDetect.getState();
        }
        switch(currentState) {
            case IN:
                motor.setPower(inPower);
                break;
            case OUT:
                if(!reset) {
                    reset = true;
                    timer.reset();
                }
                motor.setPower(outPower);
                break;
            case STOP:
                motor.setPower(0);
                break;
        }
    }
}
