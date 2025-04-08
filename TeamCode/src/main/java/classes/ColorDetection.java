package classes;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Config
public class ColorDetection {

    ColorRangeSensor sensor;

    public enum Team {
        RED,
        BLUE
    }
    Team team;
    public ColorDetection(HardwareMap hmap, Team currentTeam) {
        team = currentTeam;
        sensor = hmap.get(ColorRangeSensor.class, "ColorSensor");
    }

    public void updateTeam(Team newTeam) {
        team = newTeam;
    }
    public double getRed() {
        return sensor.red();
    }
    public double getBlue() {
        return sensor.blue();
    }
    public double getDistance() {
        return sensor.getDistance(DistanceUnit.MM);
    }
    public static int distanceThreshold = 60;
    public static int blueThreshold = 240, redThreshold = 100, yellowRedThreshold = 200, yellowBlueThreshold = 160;
    public Intake.State getState() {
        if(team == Team.RED) {
            if(sensor.blue() > blueThreshold && sensor.red() < yellowRedThreshold)
                return Intake.State.OUT;
        }
        else {
            if (sensor.red() > redThreshold && sensor.blue() < yellowBlueThreshold)
                return Intake.State.OUT;
        }
        if(sensor.getDistance(DistanceUnit.MM) < distanceThreshold)
            return Intake.State.STOP;

        return Intake.State.IN;
    }
}
