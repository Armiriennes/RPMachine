package net.zyuiop.rpmachine.permissions;


/**
 * @author Louis Vialar
 */
public enum CityPermissions implements DirectPermission {
    // Plots
    CREATE_PLOT("créer des parcelles"),
    REDEFINE_EMPTY_PLOT("redéfinir des parcelles inhabitées"),
    REDEFINE_OCCUPIED_PLOT("redéfinir des parcelles habitées"),
    DELETE_EMPTY_PLOT("supprimer des parcelles inhabitées"),
    DELETE_OCCUPIED_PLOT("supprimer des parcelles habitées"),
    CHANGE_PLOT_MEMBERS("changer les membres d'une parcelle"),

    // Territory
    EXPAND_CITY("agrandir la ville"),

    // Members
    INVITE_MEMBER("inviter un joueur dans la ville"),
    KICK_MEMBER("exclure un joueur de la ville"),

    // Management
    SET_TAXES("changer les impots de la ville"),
    SET_PRIVACY("changer le type de ville (publique/privée)"),
    SET_SPAWN("changer le spawn de la ville"),
    CHECK_TAXES("vérifier les impots en retard"),

    // Council
    ADD_COUNCIL("ajouter un nouveau conseiller à la ville (peut changer ses propres permissions!)"),
    REMOVE_COUNCIL("supprimer un conseiller de la ville");

    private final String description;

    CityPermissions(String description) {
        this.description = description;
    }

    @Override
    public String description() {
        return description;
    }
}
