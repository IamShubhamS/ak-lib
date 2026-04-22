def call(Map config = [:]) {
    if (!env.ACCUKNOX_ENDPOINT?.trim()) error('ACCUKNOX_ENDPOINT is required')
    if (!env.ACCUKNOX_LABEL?.trim()) error('ACCUKNOX_LABEL is required')
    if (!env.ACCUKNOX_TOKEN?.trim()) error('ACCUKNOX_TOKEN is required')

    if (isUnix()) {
        sh '''
          set -e

          if ! command -v accuknox-aspm-scanner >/dev/null 2>&1; then
            python3 -m pip install --user "https://github.com/accuknox/aspm-scanner-cli/releases/download/v0.14.2/accuknox_aspm_scanner-0.14.2-py3-none-any.whl"
          fi

          export PATH="$HOME/.local/bin:$PATH"
          accuknox-aspm-scanner tool install --type sast
          accuknox-aspm-scanner --softfail scan sast --command "scan ."
        '''
    } else {
        powershell '''
          $ErrorActionPreference = "Stop"

          $scanner = "C:\\Users\\VC\\AppData\\Local\\Programs\\Python\\Python311\\Scripts\\accuknox-aspm-scanner.exe"

          if (-not (Test-Path $scanner)) {
            py -m pip install "https://github.com/accuknox/aspm-scanner-cli/releases/download/v0.14.2/accuknox_aspm_scanner-0.14.2-py3-none-any.whl"
          }

          & $scanner tool install --type sast
          & $scanner --softfail scan sast --command "scan ."

          if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
        '''
    }
}
