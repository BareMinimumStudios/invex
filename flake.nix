{
  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixpkgs-unstable";
  };

  outputs = {nixpkgs, ...} @ inputs: let
    lib = nixpkgs.lib;
    supportedSystems = ["x86_64-linux" "aarch64-linux" "x86_64-darwin" "aarch64-darwin"];
    forEachSupportedSystem = f:
      lib.genAttrs supportedSystems (system:
        f {
          pkgs = import nixpkgs {inherit system;};
        });
  in {
    devShells = forEachSupportedSystem ({pkgs}: let
      java21 = pkgs.jetbrains.jdk-no-jcef-21;
      java25 = pkgs.jetbrains.jdk-no-jcef;

      nativeBuildInputs = [
        java21
        java25
      ];

      buildInputs = with pkgs; [
        libGL
        glfw3-minecraft
        flite
        libpulseaudio
      ];
    in {
      default = pkgs.mkShell {
        inherit nativeBuildInputs buildInputs;

        env = {
          LD_LIBRARY_PATH = lib.makeLibraryPath buildInputs;
          JAVA_HOME = "${java21.home}";
          JDK21 = "${java21.home}";
          JDK25 = "${java25.home}";
        };
      };
    });
  };
}
