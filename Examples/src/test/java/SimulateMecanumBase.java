import org.codeblooded.simhardware.SimHardwareMap;
import org.codeblooded.simhardware.drivetrain.SimulatedMecanum;
import org.codeblooded.simulator.RobotGeometry;
import org.codeblooded.simulator.SimConfig;
import org.codeblooded.simhardware.drivetrain.SimMecanumConfig;
import org.codeblooded.input.DefaultKeybinds;
import org.codeblooded.simulator.DriverStationSimulator;
import org.junit.Test;
import java.io.IOException;

public class SimulateMecanumBase {
    @Test
    public void test() throws IOException, InterruptedException {
        SimHardwareMap simHardwareMap = new SimHardwareMap();

        SimMecanumConfig mecanumConfig = new SimMecanumConfig();
        mecanumConfig.frontLeftMotorName = "frontLeft";
        mecanumConfig.frontRightMotorName = "frontRight";
        mecanumConfig.backLeftMotorName = "backLeft";
        mecanumConfig.backRightMotorName = "backRight";
        mecanumConfig.wheelbase = 16;
        mecanumConfig.trackWidth = 16;
        mecanumConfig.wheelRadius = 1.889765;
        mecanumConfig.staticVelocityRegion = 2;
        mecanumConfig.staticFriction = 45;
        mecanumConfig.maxAcceleration = 150;
        mecanumConfig.maxVelocity = 70;
        mecanumConfig.naturalDeceleration = 40;
        mecanumConfig.strafeEfficiency = 0.90;
        mecanumConfig.simHardwareMap = simHardwareMap;

        simHardwareMap.setDrivetrain(new SimulatedMecanum(mecanumConfig));
        simHardwareMap.pinpoint("pinpoint");

        SimConfig simConfig = new SimConfig();
        simConfig.gamepad1Keybinds = new DefaultKeybinds();
        simConfig.gamepad2Keybinds = new DefaultKeybinds();
        simConfig.simHardwareMap = simHardwareMap;
        simConfig.loopTimeMs = 20;
        simConfig.robotGeometry = new RobotGeometry(18, 18, 0, 0);

        DriverStationSimulator driverStation = new DriverStationSimulator(simConfig);
    }
}