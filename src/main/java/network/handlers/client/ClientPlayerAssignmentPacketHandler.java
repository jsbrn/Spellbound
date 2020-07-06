package network.handlers.client;

import com.esotericsoftware.kryonet.Connection;
import gui.states.GameScreen;
import gui.states.GameState;
import main.GameManager;
import network.Packet;
import network.PacketHandler;
import network.packets.PlayerAssignmentPacket;
import world.Camera;

public class ClientPlayerAssignmentPacketHandler implements PacketHandler {
    @Override
    public boolean handle(Packet p, Connection from) {
        PlayerAssignmentPacket pap = (PlayerAssignmentPacket)p;
        Camera.setTargetEntity(pap.entityID);
        ((GameScreen)GameManager.getGameState(GameState.GAME_SCREEN)).setTarget(pap.entityID);
        GameManager.switchTo(GameState.GAME_SCREEN, true);
        return true;
    }
}
