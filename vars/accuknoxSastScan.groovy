def call(Map config = [:]) {
    String image = config.get('image', 'gcshubham/accuknox-sast-jenkins:0.1')
    String softfail = config.get('softfail', 'true').toString()
    if (!env.ACCUKNOX_ENDPOINT?.trim()) {
        error('ACCUKNOX_ENDPOINT is required')
    }
    if (!env.ACCUKNOX_LABEL?.trim()) {
        error('ACCUKNOX_LABEL is required')
    }
    if (!env.ACCUKNOX_TOKEN?.trim()) {
        error('ACCUKNOX_TOKEN is required')
    }
    sh """
      set -e
      docker pull ${image}
      docker run --rm \\
        -e SOFTFAIL=${softfail} \\
        -e ACCUKNOX_ENDPOINT="\$ACCUKNOX_ENDPOINT" \\
        -e ACCUKNOX_LABEL="\$ACCUKNOX_LABEL" \\
        -e ACCUKNOX_TOKEN="\$ACCUKNOX_TOKEN" \\
        -v "\$WORKSPACE:/workspace" \\
        ${image}
    """
}
