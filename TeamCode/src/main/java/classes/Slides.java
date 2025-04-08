package classes;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

@Config
public class Slides {
    DcMotorEx motorLeft, motorRight;
    PIDController controller;

    public static double p, i, d;
    public Slides(HardwareMap hmap) {
        motorLeft = hmap.get(DcMotorEx.class, "motorLeft");
        motorRight = hmap.get(DcMotorEx.class, "motorRight");
        controller = new PIDController(p, i, d);
        motorRight.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public enum SlidesState {
        RETRACTED,
        MOVING,
        EXTENDED,
        RETRACTING
    }
    SlidesState currentState;
    public static double inferiorLimit, superiorLimit, targetPos, power, pid;
    public void update() {
        switch(currentState) {
            case RETRACTED:
                currentState = SlidesState.MOVING;
                break;
            case MOVING:
                controller.setPID(p, i, d);
                controller.setTolerance(0);
                pid = controller.calculate(motorLeft.getCurrentPosition(), targetPos);

                power = Math.max(inferiorLimit, power);
                power = Math.min(superiorLimit, power);

                motorLeft.setPower(pid);

                if(motorLeft.getCurrentPosition() == targetPos)
                    currentState = SlidesState.EXTENDED;
                break;
            case EXTENDED:
                currentState = SlidesState.RETRACTING;
                targetPos = 0;
                break;
            case RETRACTING:
                controller.setPID(p, i, d);
                controller.setTolerance(0);
                pid = controller.calculate(motorLeft.getCurrentPosition(), targetPos);

                power = Math.max(inferiorLimit, power);
                power = Math.min(superiorLimit, power);

                motorLeft.setPower(pid);

                if(motorLeft.getCurrentPosition() == targetPos)
                    currentState = SlidesState.RETRACTED;
                break;
        }
    }
}
