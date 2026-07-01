import com.qualcomm.robotcore.hardware.DcMotor;
import org.jjophoven.simulator.SimulationConfig;
import org.jjophoven.fakehardware.drivetrain.MecanumConfig;
import org.jjophoven.input.Keybinds;
import org.jjophoven.simulator.DriverStationSimulator;
import org.junit.Test;
import java.io.IOException;

public class SimulateOpMode {
    @Test
    public void test() throws IOException, InterruptedException {
        SimulationConfig simulationConfig = new SimulationConfig();

        MecanumConfig mecanumConfig = new MecanumConfig();
        mecanumConfig.frontLeftMotorName = "frontLeft";
        mecanumConfig.frontRightMotorName = "frontRight";
        mecanumConfig.backLeftMotorName = "backLeft";
        mecanumConfig.backRightMotorName = "backRight";
        mecanumConfig.wheelbase = 4.68504; // half distance from frontLeft wheel to backLeft wheel
        mecanumConfig.trackWidth = 4.56693; // half distance from backRight wheel to backLeft wheel
        mecanumConfig.wheelDiameter = 3.77953;
        mecanumConfig.staticVelocityRegion = 2;
        mecanumConfig.staticFriction = 45;
        mecanumConfig.maxAcceleration = 250;
        mecanumConfig.maxVelocity = 70;
        mecanumConfig.naturalDeceleration = 40;
        mecanumConfig.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE;
        mecanumConfig.strafeEfficiency = 0.90;

        simulationConfig.drivetrain = mecanumConfig;
        simulationConfig.gamepad1Keybinds = new Keybinds();
        simulationConfig.gamepad2Keybinds = new Keybinds();

        DriverStationSimulator driverStation = new DriverStationSimulator(simulationConfig);
    }
}
