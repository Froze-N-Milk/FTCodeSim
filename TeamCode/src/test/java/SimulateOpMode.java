import org.jjophoven.simulator.SimulationConfig;
import org.jjophoven.fakehardware.drivetrain.MecanumConfig;
import org.jjophoven.input.Keybinds;
import org.jjophoven.simulator.DriverStationSimulator;
import org.junit.Test;
import java.io.IOException;

public class SimulateOpMode {
    @Test
    public void test() throws IOException, InterruptedException {
        double accelK = 0.7;
        double dutyBackEMF = 1.5;
        double viscousFriction = 0.7;
        double coulombFriction = 3; // rolling friction?

        SimulationConfig simulationConfig = new SimulationConfig();

        MecanumConfig mecanumConfig = new MecanumConfig();
        mecanumConfig.frontLeftMotorName = "frontLeft";
        mecanumConfig.frontRightMotorName = "frontRight";
        mecanumConfig.backLeftMotorName = "backLeft";
        mecanumConfig.backRightMotorName = "backRight";
        mecanumConfig.wheelbase = 0.2286;
        mecanumConfig.trackWidth = 0.2286;
        mecanumConfig.coefficients = new double[]{accelK, dutyBackEMF, viscousFriction, coulombFriction};
        mecanumConfig.staticVelocityRegion = 0.05;
        mecanumConfig.staticFriction = 4.5;

        simulationConfig.drivetrain = mecanumConfig;
        simulationConfig.gamepad1Keybinds = new Keybinds();
        simulationConfig.gamepad2Keybinds = new Keybinds();

        DriverStationSimulator driverStation = new DriverStationSimulator(simulationConfig);
    }
}

