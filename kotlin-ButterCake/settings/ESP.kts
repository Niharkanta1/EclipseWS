import com.buttercake.game.Color
import com.charlatano.settings.*;

///////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////// --- ESP Types --- ///////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////

/**
 * Whether or not to use skeleton ESP.
 */
SKELETON_ESP = false

/**
 * Whether or not to use box ESP.
 */
BOX_ESP = true

/**
 * Whether or not to change model colors
 */
CHAMS = false

/**
 * Brightness of CHAMS
 */
CHAMS_BRIGHTNESS = 100


///////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////// --- TOGGLES --- ////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////

/**
 * Whether or not to highlight your team mates.
 */
SHOW_TEAM = true

/**
 * Whether or not to highlight enemies.
 */
SHOW_ENEMIES = true

/**
 * Whether or not to highlight "dormant" (unknown-location) players.
 *
 * Enabling this can allow you to see players at a further distance,
 * but you may see some "ghost" players which are really not there.
 */
SHOW_DORMANT = false

/**
 * Whether or not to highlight weapons.
 */
SHOW_WEAPONS = false

/**
 * Whether or not to highlight grenades.
 */
SHOW_GRENADES = false


///////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////// --- COLORS --- ///////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////

/**
 * The color to highlight your team mates.
 */
TEAM_COLOR = Color(0, 0, 255, 1.0)

/**
 * The color to highlight your enemies.
 */
ENEMY_COLOR = Color(255, 0, 0, 1.0)

/**
 * The color to highlight weapons.
 */
WEAPON_COLOR = Color(0, 255, 0, 0.5)

/**
 * The color to highlight grenades.
 */
GRENADE_COLOR = Color(0, 255, 0, 1.0)