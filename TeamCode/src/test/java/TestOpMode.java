import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.teamcode.fake.FakeHardwareMap;
import org.firstinspires.ftc.teamcode.fake.FakeTelemetry;
import org.firstinspires.ftc.teamcode.opmode.base.TeleOpMode;
import org.junit.Test;
import org.psilynx.psikit.ftc.autolog.PsiKitAutoLog;
import org.psilynx.psikit.ftc.wrappers.GamepadWrapper;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

@PsiKitAutoLog
//@Config(shadows = {ShadowAppUtil.class})
//@RunWith(RobolectricTestRunner.class)
public class TestOpMode {
//    static {
//        System.setProperty("ROBOTCORE_LOG_LEVEL", "SILENT");
//    }

    @Test
    public void test() {

        OpMode opMode = new TeleOpMode() {
            @Override
            protected void onFirstDriverInput() {

            }
        };

        opMode.hardwareMap = new FakeHardwareMap(null, null);
        opMode.gamepad1 = new GamepadWrapper(opMode.gamepad1);
        opMode.telemetry = new FakeTelemetry();

        opMode.init();

        for (int i = 0; i < 10; i++) {
            opMode.init_loop();

            // simulate gamepad input from keyboard
            //opMode.gamepad1.
        }

        opMode.start();

        String data = "Users Input" +
                "\nA second line of user input.";
        System.setIn(new ByteArrayInputStream(data.getBytes()));

        Scanner scanner = new Scanner(System.in);

        //for (int i = 0; i < 100; i++) {
        while (true) {
            opMode.loop();

        }

        //opMode.stop();
    }
}
