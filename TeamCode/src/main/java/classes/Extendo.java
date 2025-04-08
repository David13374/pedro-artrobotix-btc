package classes;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.PIDController;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

@Config
public class Extendo {
    DcMotorEx extendo;

    public static int p, i, d;
    public Extendo(HardwareMap hmap) {
        extendo = hmap.get(DcMotorImplEx.class, "extendo");
        controller = new PIDController(p, i, d);
    }

    public enum extendoState {
        EXTENDED,
        RETRACTED
    }

    private PIDController controller;
    extendoState currExtendoState;
    public static double Pos, extendoConstant;
    double power;
    public static double inferiorLimit, superiorLimit;
    public static double retractedPos, extendedPos;

    public void update(GamepadEx gamepad) {
        controller.setPID(p, i, d);
        controller.setTolerance(0);
        if(currExtendoState == extendoState.EXTENDED) {
            Pos += gamepad.getRightY() * extendoConstant;
            double pid = controller.calculate(extendo.getCurrentPosition(), Pos);

            power = Math.max(inferiorLimit, power);
            power = Math.min(superiorLimit, power);

            extendo.setPower(pid);
        }
        else {
            double pid = controller.calculate(extendo.getCurrentPosition(), retractedPos);

            power = Math.max(inferiorLimit, power);
            power = Math.min(superiorLimit, power);

            extendo.setPower(pid);
        }
    }

    public void updateState() {
        switch(currExtendoState) {
            case EXTENDED:
                currExtendoState = extendoState.RETRACTED;
                Pos = extendedPos;
                break;
            case RETRACTED:
                currExtendoState = extendoState.EXTENDED;
                break;
        }
    }
}
