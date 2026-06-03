# InvEx

<p style="text-align: center">
  <!--<a href="">
    <img alt="curseforge" height="40" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/available/curseforge_vector.svg">
  </a>
  <a href="">
    <img alt="modrinth" height="40" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/available/modrinth_vector.svg">
  </a>-->
  <img alt="fabric" height="40" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/supported/fabric_vector.svg">
  <a href="https://discord.gg/pcRw79hwey">
    <img alt="discord-plural" height="40" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/social/discord-plural_vector.svg">
  </a>
  <a href="https://github.com/BareMinimumStudios/invex">
    <img alt="github" height="40" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/available/github_vector.svg">
  </a>
  <a href="https://ko-fi.com/naomieow">
    <img alt="kofi-singular" height="40" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/donate/kofi-singular_vector.svg">
  </a>
</p>

---

InvEx is a server-side inventory viewer/explorer with support for offline players, [Trinkets](https://modrinth.com/mod/trinkets) and [Accessories](https://modrinth.com/mod/accessories). 

## Commands

- `/invsee <name>` - Inventory view
- `/endsee <name>` - Ender Chest view
- `/accsee <name>` - Accessories view, only enabled when Accessories is installed
- `/trinketsee <name>` - Trinkets view, only enabled when Trinkets is installed, disabled when Accessories compatability mod is also enabled.

## Permissions 

InvEx makes use of the common Permissions API which can be configured using mods such as [LuckPerms](https://modrinth.com/mod/luckperms), but it also has support for Minecraft's built in [permission levels](https://minecraft.wiki/w/Permission_level) for servers that aren't using those mods.

<details>
  <summary>Permissions API</summary>
  <p>All the permission IDs and their relevant descriptions can be seen in <a href="https://github.com/BareMinimumStudios/invex/blob/main/src/main/kotlin/xyz/naomieow/invex/InvExPermissions.kt">InvExPermissions.kt</a>.</p>
</details>

<details>
  <summary>Vanilla</summary>
  <ul>
  <li>Anyone with OP permission level 2 will be able to view inventories, but not modify them.</li>
  <li>Anyone with OP permission level 3 or higher will be able to modify inventories, and is immune to being viewed/modified.</li>
  </ul>
</details>

## License

InvEx is licensed under the Bare Minimum License v1.0, which can be found in [LICENSE](LICENSE) or [here](https://github.com/BareMinimumStudios/bare-minimum-license).
