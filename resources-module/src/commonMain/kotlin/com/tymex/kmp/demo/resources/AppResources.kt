package com.tymex.kmp.demo.resources

/**
 * App resources definitions - designed for compileOnly usage
 * 
 * This module contains resource identifiers and constants that are used
 * across the application. It's designed to be used with compileOnly because:
 * 
 * 1. Resource files are bundled separately by platform build systems
 * 2. Only resource identifiers are needed at compile time
 * 3. Actual resource loading is handled by platform-specific mechanisms
 * 4. Reduces binary size by not duplicating resource references
 */

/**
 * Image resource identifiers
 * These correspond to actual image files in platform-specific resource folders
 */
object Images {
    // App icons
    const val APP_ICON = "app_icon"
    const val APP_LOGO = "app_logo"
    const val APP_SPLASH = "app_splash"
    
    // User interface icons
    const val IC_USER = "ic_user"
    const val IC_SETTINGS = "ic_settings"
    const val IC_LOGOUT = "ic_logout"
    const val IC_BACK = "ic_back"
    const val IC_FORWARD = "ic_forward"
    const val IC_REFRESH = "ic_refresh"
    
    // Payment icons
    const val IC_CREDIT_CARD = "ic_credit_card"
    const val IC_BANK_TRANSFER = "ic_bank_transfer"
    const val IC_DIGITAL_WALLET = "ic_digital_wallet"
    const val IC_PAYMENT_SUCCESS = "ic_payment_success"
    const val IC_PAYMENT_FAILED = "ic_payment_failed"
    
    // Feature illustrations
    const val ILLUSTRATION_EMPTY_STATE = "illustration_empty_state"
    const val ILLUSTRATION_ERROR = "illustration_error"
    const val ILLUSTRATION_SUCCESS = "illustration_success"
    
    // Background images
    const val BG_LOGIN = "bg_login"
    const val BG_MAIN = "bg_main"
    const val BG_PAYMENT = "bg_payment"
}

/**
 * String resource identifiers
 * These correspond to localized strings in platform-specific resource files
 */
object Strings {
    // App general
    const val APP_NAME = "app_name"
    const val APP_VERSION = "app_version"
    
    // Authentication
    const val LOGIN_TITLE = "login_title"
    const val LOGIN_EMAIL_HINT = "login_email_hint"
    const val LOGIN_PASSWORD_HINT = "login_password_hint"
    const val LOGIN_BUTTON = "login_button"
    const val LOGOUT_BUTTON = "logout_button"
    const val FORGOT_PASSWORD = "forgot_password"
    
    // User management
    const val USER_PROFILE = "user_profile"
    const val USER_SETTINGS = "user_settings"
    const val USER_NAME_LABEL = "user_name_label"
    const val USER_EMAIL_LABEL = "user_email_label"
    const val USER_SAVE_BUTTON = "user_save_button"
    
    // Payment
    const val PAYMENT_TITLE = "payment_title"
    const val PAYMENT_AMOUNT_LABEL = "payment_amount_label"
    const val PAYMENT_METHOD_LABEL = "payment_method_label"
    const val PAYMENT_PROCESS_BUTTON = "payment_process_button"
    const val PAYMENT_SUCCESS_MESSAGE = "payment_success_message"
    const val PAYMENT_FAILED_MESSAGE = "payment_failed_message"
    
    // Error messages
    const val ERROR_NETWORK = "error_network"
    const val ERROR_INVALID_INPUT = "error_invalid_input"
    const val ERROR_UNAUTHORIZED = "error_unauthorized"
    const val ERROR_GENERIC = "error_generic"
    
    // Common actions
    const val ACTION_OK = "action_ok"
    const val ACTION_CANCEL = "action_cancel"
    const val ACTION_RETRY = "action_retry"
    const val ACTION_SAVE = "action_save"
    const val ACTION_DELETE = "action_delete"
}

/**
 * Color resource identifiers
 */
object Colors {
    // Primary colors
    const val PRIMARY = "primary"
    const val PRIMARY_DARK = "primary_dark"
    const val PRIMARY_LIGHT = "primary_light"
    
    // Secondary colors
    const val SECONDARY = "secondary"
    const val SECONDARY_DARK = "secondary_dark"
    const val SECONDARY_LIGHT = "secondary_light"
    
    // Status colors
    const val SUCCESS = "success"
    const val ERROR = "error"
    const val WARNING = "warning"
    const val INFO = "info"
    
    // Text colors
    const val TEXT_PRIMARY = "text_primary"
    const val TEXT_SECONDARY = "text_secondary"
    const val TEXT_HINT = "text_hint"
    
    // Background colors
    const val BACKGROUND = "background"
    const val SURFACE = "surface"
    const val CARD_BACKGROUND = "card_background"
}

/**
 * Dimension resource identifiers
 */
object Dimensions {
    // Margins and padding
    const val MARGIN_SMALL = "margin_small"
    const val MARGIN_MEDIUM = "margin_medium"
    const val MARGIN_LARGE = "margin_large"
    
    const val PADDING_SMALL = "padding_small"
    const val PADDING_MEDIUM = "padding_medium"
    const val PADDING_LARGE = "padding_large"
    
    // Text sizes
    const val TEXT_SIZE_SMALL = "text_size_small"
    const val TEXT_SIZE_MEDIUM = "text_size_medium"
    const val TEXT_SIZE_LARGE = "text_size_large"
    const val TEXT_SIZE_TITLE = "text_size_title"
    
    // Component sizes
    const val BUTTON_HEIGHT = "button_height"
    const val INPUT_HEIGHT = "input_height"
    const val CARD_ELEVATION = "card_elevation"
    const val CORNER_RADIUS = "corner_radius"
}

/**
 * Resource utility functions
 * These provide compile-time validation of resource identifiers
 */
object ResourceValidator {
    
    /**
     * Validate image resource identifier
     */
    fun validateImageResource(resourceId: String): Boolean {
        return Images::class.java.declaredFields
            .any { field -> 
                field.isAccessible = true
                field.get(null) == resourceId 
            }
    }
    
    /**
     * Validate string resource identifier
     */
    fun validateStringResource(resourceId: String): Boolean {
        return Strings::class.java.declaredFields
            .any { field -> 
                field.isAccessible = true
                field.get(null) == resourceId 
            }
    }
    
    /**
     * Get all image resource identifiers
     */
    fun getAllImageResources(): List<String> {
        return Images::class.java.declaredFields
            .map { field ->
                field.isAccessible = true
                field.get(null) as String
            }
    }
    
    /**
     * Get all string resource identifiers
     */
    fun getAllStringResources(): List<String> {
        return Strings::class.java.declaredFields
            .map { field ->
                field.isAccessible = true
                field.get(null) as String
            }
    }
}
