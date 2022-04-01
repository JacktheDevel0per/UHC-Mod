package net.casualuhc.uhcmod.utils;

import net.casualuhc.uhcmod.UHCMod;
import net.casualuhc.uhcmod.managers.GameManager;
import net.casualuhc.uhcmod.managers.TeamManager;
import net.casualuhc.uhcmod.managers.WorldBorderManager;

public record Phase(Runnable runnable, int phaseNumber) {

    private static final ThreadGroup PHASE_THREAD_GROUP = new ThreadGroup("Phase Thread Group");

    public static final Phase
        NONE = new Phase(() -> { }, 0),
        SETUP = new Phase(Phase::setup, 1),
        LOBBY = new Phase(Phase::lobby, 2),
        READY = new Phase(Phase::ready, 3),
        START = new Phase(Phase::start, 4),
        ACTIVE = new Phase(Phase::active, 5),
        END = new Phase(Phase::end, 6);

    public void run() {
        PHASE_THREAD_GROUP.interrupt();
        GameManager.setCurrentPhase(this);
        this.runnable.run();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Phase phase) {
            return phase.phaseNumber == this.phaseNumber;
        }
        return false;
    }

    private static void setup() {
        GameManager.setBeforeGamerules();
        TeamManager.createTeams();
    }

    private static void lobby() {
        GameManager.generateLobby();
        PlayerUtils.forEveryPlayer(playerEntity -> playerEntity.teleport(UHCMod.UHC_SERVER.getOverworld(), 0, 253, 0, 0, 0));
    }

    private static void ready() {
        TeamUtils.unReadyAllTeams();
        TeamUtils.sendReadyMessage();
    }

    private static void start() {
        GameManager.startCountDown(PHASE_THREAD_GROUP);
    }

    private static void active() {
        WorldBorderManager.startWorldBorders(PHASE_THREAD_GROUP, false);
        GameManager.startPVPCountdown(PHASE_THREAD_GROUP);
        GameManager.setUHCGamerules();
    }

    private static void end() {
        GameManager.endUHC(PHASE_THREAD_GROUP);
    }

    public static ThreadGroup getPhaseThreadGroup() {
        return PHASE_THREAD_GROUP;
    }
}
