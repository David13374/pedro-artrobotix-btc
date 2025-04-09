package classes;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.concurrent.TimeUnit;

@Config
public class ColorDetection {

    ColorRangeSensor sensor;

    public enum Team {
        RED,
        BLUE
    }
    public static Team team;
    public ColorDetection(HardwareMap hmap, Team currentTeam) {
        team = currentTeam;
        sensor = hmap.get(ColorRangeSensor.class, "ColorSensor");
        time1= new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
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
    public double getGreen() {return sensor.green();}
    public double getDistance() {
        return sensor.getDistance(DistanceUnit.MM);
    }
    public static int distanceThreshold = 60;
    public static int blueThreshold = 140, redThreshold = 100, yellowGreenThreshold = 106, yellowGreenThresholdBlue = 300;
    public static int waitTime = 200;
    ElapsedTime time1;
    boolean started = false;
    public Intake.State getState() {
        Intake.State returnState = Intake.State.IN;
        if(team == Team.RED) {
            if(sensor.blue() > blueThreshold && sensor.green() < yellowGreenThresholdBlue)
                returnState = Intake.State.OUT;
        }
        else {
            if (sensor.red() > redThreshold && sensor.green() < yellowGreenThreshold)
                returnState = Intake.State.OUT;
        }
        if(sensor.getDistance(DistanceUnit.MM) < distanceThreshold && !started) {
            time1.reset();
            time1.startTime();
            started = true;
        }

        if(time1.time(TimeUnit.MILLISECONDS) > waitTime) {
            if(sensor.getDistance(DistanceUnit.MM) > distanceThreshold) {
                time1.reset();
                started = false;
            }
            if(returnState == Intake.State.IN)
                return Intake.State.STOP;
            return returnState;
        }
        return returnState;
    }
}