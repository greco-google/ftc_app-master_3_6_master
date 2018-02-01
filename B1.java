package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;

/**
 * Created by zahs on 9/28/2017.
 */
@Autonomous (name = "Blue1" , group = "test")
public class B1 extends OpMode{
        ImportantStuff robot = new ImportantStuff();
        private enum State
        {
            STATE_GLYPHUP,
            STATE_OUT,
            STATE_DOWN,
            STATE_RELIC,
            STATE_GLYPHUP2,
            STATE_UP,
            STATE_IN,
            STATE_SERVO,
            STATE_INITIAL,
            STATE_SERVO2,
            STATE_COLOR,
            STATE_STOP,
        }
        private State CurrentState;
        ElapsedTime Runtime = new ElapsedTime();
    private enum State2
    {
        STATE_BACKWARDS,
        STATE_FORWARDS
    }
    private State2 CurrentState2;

    public B1(){}

        @Override
        public void init() {
            robot.init(hardwareMap, telemetry);
            robot.robotStuff.servoColor.setPosition(robot.robotStuff.SERVO_LATCH_UP);
            robot.robotStuff.servoColor2.setPosition(robot.robotStuff.SERVO_LATCH_UP);
            robot.robotStuff.servoGlyphBL.setPosition(robot.robotStuff.SERVO_LATCH_DOWN);
            robot.robotStuff.servoGlyphBR.setPosition(robot.robotStuff.SERVO_LATCH_UP);
}

    @Override
    public void start() {
        super.start();
        Runtime.reset();
        newState(State. STATE_OUT);
        newState2(State2.STATE_BACKWARDS);
    }

    @Override
    public void loop() {
        robot.robotStuff.colorSensor2.red();
        robot.robotStuff.colorSensor2.green();
        robot.robotStuff.colorSensor2.blue();
        robot.robotStuff.colorSensor2.alpha();
        robot.robotStuff.colorSensor2.argb();
        switch (CurrentState) {
            case STATE_OUT:
                if (Runtime.milliseconds() > 250) {
                    robot.robotStuff.relicArmExtends.setPower(1);
                    newState(State.STATE_DOWN);

                }
                break;
            case STATE_DOWN:
                if (Runtime.milliseconds() > 2100) {
                    robot.robotStuff.relicArmExtends.setPower(0);
                    robot.robotStuff.relicArmLift.setPower(1);
                    newState(State.STATE_RELIC);
                }
                break;
            case STATE_RELIC:
                if (Runtime.milliseconds() > 300) {
                    robot.robotStuff.relicArmLift.setPower(0);
                    robot.robotStuff.servoRelic.setPosition(robot.robotStuff.SERVO_LATCH_DOWN);
                    newState(State.STATE_UP);
                }
                break;
            case STATE_UP:
                if (Runtime.milliseconds() > 1000) {
                    robot.robotStuff.relicArmLift.setPower(-1);
                    newState(State.STATE_IN);
                }
                break;
            case STATE_IN:
                if (Runtime.milliseconds() > 1000) {
                    robot.robotStuff.relicArmLift.setPower(0);
                    robot.robotStuff.relicArmExtends.setPower(-1);
                    newState(State.STATE_SERVO);
                }
                break;
            case STATE_SERVO:
                if (Runtime.milliseconds() > 250) {  //was at 2000, change back to that
                    robot.robotStuff.relicArmExtends.setPower(0);
                    robot.robotStuff.servoColor2.setPosition(robot.robotStuff.SERVO_LATCH_DOWN);
                    newState(State.STATE_GLYPHUP);
                }
                break;
            case STATE_GLYPHUP:
                if (Runtime.milliseconds() > 1000) {
                    robot.robotStuff.glyphArm.setPower(-0.5);
                    newState(State.STATE_INITIAL);
                }
                break;
            case STATE_INITIAL:
                if (Runtime.milliseconds() > 500) {
                    robot.robotStuff.glyphArm.setPower(0);
                    while (20 < robot.robotStuff.colorSensor2.red()) {
                        switch (CurrentState2) {
                            case STATE_BACKWARDS:
                                if (Runtime.milliseconds() > 1000) {
                                    robot.robotStuff.FLeft.setPower(-1);
                                    robot.robotStuff.FRight.setPower(-1);
                                    robot.robotStuff.BLeft.setPower(-1);
                                    robot.robotStuff.BRight.setPower(-1);
                                    newState2(State2.STATE_FORWARDS);
                                }
                                break;
                            case STATE_FORWARDS:
                                if (Runtime.milliseconds() > 30) {
                                    robot.robotStuff.FLeft.setPower(1);
                                    robot.robotStuff.FRight.setPower(1);
                                    robot.robotStuff.BLeft.setPower(1);
                                    robot.robotStuff.BRight.setPower(1);
                                    newState(State.STATE_SERVO2);
                                }
                                break;

                        }

                    }
                    while (20 < robot.robotStuff.colorSensor2.blue()) {
                        robot.robotStuff.FLeft.setPower(1);
                        robot.robotStuff.FRight.setPower(1);
                        robot.robotStuff.BLeft.setPower(1);
                        robot.robotStuff.BRight.setPower(1);
                    }
                    newState(State.STATE_SERVO2);
                }
                break;
            case STATE_SERVO2:
                if (Runtime.milliseconds() > 30) {
                    robot.stop();
                    robot.robotStuff.servoColor2.setPosition(robot.robotStuff.SERVO_LATCH_UP);
                    newState(State.STATE_GLYPHUP2);
                }
                break;
            case STATE_GLYPHUP2:
                if (Runtime.milliseconds() > 500) {
                    robot.robotStuff.glyphArm.setPower(-0.5);
                    newState(State.STATE_COLOR);
                }
                break;
            case STATE_COLOR:
                if (Runtime.milliseconds() > 500) {
                    robot.robotStuff.glyphArm.setPower(0);
                    robot.robotStuff.FLeft.setPower(1);
                    robot.robotStuff.FRight.setPower(1);
                    robot.robotStuff.BLeft.setPower(1);
                    robot.robotStuff.BRight.setPower(1);
                    newState(State.STATE_STOP);
                }
                break;
            case STATE_STOP:
                if (Runtime.milliseconds() > 250) {
                robot.stop();
                robot.robotStuff.relicArmLift.setPower(0);
                robot.robotStuff.relicArmExtends.setPower(0);}
                break;
        }
        telemetry.addData("state", CurrentState);
        telemetry.addData("FLeft", robot.robotStuff.FLeft.getCurrentPosition());
        telemetry.addData("FRight", robot.robotStuff.FRight.getCurrentPosition());

    }
        String format(OpenGLMatrix transformationMatrix) {
            return (transformationMatrix != null) ? transformationMatrix.formatAsTransform() : "null";

    }

    private void newState(State newState)
    {
        Runtime.reset();
        CurrentState = newState;
    }
    private void newState2 (State2 newState2) {
        Runtime.reset();
        CurrentState2 = newState2;
    }

    @Override
    public void stop() {
        super.stop();
        robot.stop();
    }
}
