package xyz.naomieow.invex

object InvExPermissions {
    // Can use /endsee
    const val ENDSEE_COMMAND: String = "invex.endsee.command"
    // Can modify in /endsee
    const val ENDSEE_MODIFY: String = "invex.endsee.modify"
    // Can use /invsee
    const val INVSEE_COMMAND: String = "invex.invsee.command"
    // Can modify in /invsee
    const val INVSEE_MODIFY: String = "invex.invsee.modify"
    // Can use /trinketsee
    const val TRINKETSEE_COMMAND: String = "invex.trinketsee.command"
    // Can modify in /trinketsee
    const val TRINKETSEE_MODIFY: String = "invex.trinketsee.modify"
    // Is 'immune' to being modified
    const val IMMUNE_MODIFY: String = "invex.immune.modify"
    // Is 'immune' to being viewed
    const val IMMUNE_VIEW: String = "invex.immune.view"
}