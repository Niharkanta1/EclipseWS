package com.overwolf.offsets;

public final class Offsets {
	/**
	 * Offsets
	 */
	public static long m_dwGlowObject;

	public static final InputOffsets input = new InputOffsets();

	public static long m_dw_bOverridePostProcessingDisable;
	public static long m_dwPlayerResourcesPointer;
	public static long m_dwPlayerResources;
	public static long m_dwEntityList;
	public static long m_dwLocalPlayer;
	public static long m_dwLocalPlayerPointer; // < dereference!
	public static long m_dwGlobalVars;
	public static long m_dwGlobalVarsPointer;
	public static long m_dwGameRules;
	public static long m_dwClientClassHead;
	public static long m_dwServerDetail;
	public static long m_dwViewAngleBasePointer;

	public static long m_dwClientState;
	public static long m_dwModelCache;
	//public static long m_szGameDirectory;
	public static String modDirectory;

	// Globals
	public static long g_vecCurrentRenderOrigin;
	public static long g_vecCurrentRenderAngles;
	public static long g_matCurrentCamInverse;
	public static long g_MDLCache; // CMDLCache
	
	/*
	 * Static offsets
	 */

	public static long m_dwEntityLoopDistance = 0x20;
	public static long m_dwBoneDistance = 0x30;

	public static long m_viewPunchAngle = 0x68;
	public static long m_aimPunchAngle = 0x74;
	public static long m_bDormant = 0x121;
	public static long m_vecViewOffset = 0x13c;
	public static long m_vecVelocity = 0x148;
	public static long m_vecBaseVelocity = 0x154;
	public static long m_bSpotted = 0xECD;
	// CBaseAnimating->m_pStudioBones 0x2C44 + 2C, DT_BaseAnimating->m_nForceBone + 2C
	public static long m_dwBoneMatrix = 0x2C70;

	public static long m_Local = 0x36f0;
	public static long m_iLastCrosshairIndex = 0xBBD4; // outdated
	public static long m_iCrosshairIndex = 0xBBE0;
	public static long m_vecViewAngles = 0x8E20;

	public static long m_nDeltaTick = 0x20C;
	public static long m_szMapFile = 0x220;
	public static long m_bIsInGame = 0x1A0;

	public static void load() {
		
	}
}

