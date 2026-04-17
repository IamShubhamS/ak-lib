def call(Map config = [:]) {
    String image = config.get('image', 'gcshubham/accuknox-secret-jenkins:0.3')
    boolean softFail = config.get('softFail', true)

    if (!env.ACCUKNOX_ENDPOINT?.trim()) {
        error('ACCUKNOX_ENDPOINT is required')
    }
    if (!env.ACCUKNOX_LABEL?.trim()) {
        error('ACCUKNOX_LABEL is required')
    }
    if (!env.ACCUKNOX_TOKEN?.trim()) {
        error('ACCUKNOX_TOKEN is required')
    }

    String cmd = """
      docker pull ${image}
      docker run --rm \\
        -e ACCUKNOX_ENDPOINT="\$ACCUKNOX_ENDPOINT" \\
        -e ACCUKNOX_LABEL="\$ACCUKNOX_LABEL" \\
        -e ACCUKNOX_TOKEN="\$ACCUKNOX_TOKEN" \\
        -v "\$WORKSPACE:/workspace" \\
        ${image}
    """.stripIndent()

    if (softFail) {
        int rc = sh(script: cmd, returnStatus: true)
        echo "akSecretScan completed with exit code: ${rc}. Continuing because softFail=true."
    } else {
        sh cmd
    }
}
