def call(Map config = [:]) {
    String image = config.get('image', 'gcshubham/accuknox-sast-jenkins:0.4')

    if (!env.ACCUKNOX_ENDPOINT?.trim()) {
        error('ACCUKNOX_ENDPOINT is required')
    }
    if (!env.ACCUKNOX_LABEL?.trim()) {
        error('ACCUKNOX_LABEL is required')
    }
    if (!env.ACCUKNOX_TOKEN?.trim()) {
        error('ACCUKNOX_TOKEN is required')
    }

      if (isUnix()) {
        sh """
          set -e
          docker pull ${image}
          docker run --rm \\
            -e ACCUKNOX_ENDPOINT="\$ACCUKNOX_ENDPOINT" \\
            -e ACCUKNOX_LABEL="\$ACCUKNOX_LABEL" \\
            -e ACCUKNOX_TOKEN="\$ACCUKNOX_TOKEN" \\
            -v "\$WORKSPACE:/workspace" \\
            ${image}
        """
    } else {
        // Windows - use PowerShell
        powershell """
          \$ErrorActionPreference = 'Stop'
          docker pull ${image}
          docker run --rm `
            -e ACCUKNOX_ENDPOINT="\$env:ACCUKNOX_ENDPOINT" `
            -e ACCUKNOX_LABEL="\$env:ACCUKNOX_LABEL" `
            -e ACCUKNOX_TOKEN="\$env:ACCUKNOX_TOKEN" `
            -v "\${env:WORKSPACE}:/workspace" `
            ${image}
        """
    }
}
