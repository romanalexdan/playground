pipeline {
    agent any

    environment {
        GRADLE_BIN = '/opt/gradle_source/bin/gradle'
        GRADLE_USER_HOME = '/var/jenkins_home/.gradle'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Initialize') {
            steps {
                sh "chmod +x ${GRADLE_BIN}"

                sh "${GRADLE_BIN} -v"
            }
        }

        stage('Build') {
            steps {
                echo 'Starting Gradle Build...'
                sh "${GRADLE_BIN} clean assemble --no-daemon"
            }
        }

        stage('Setup Configuration') {
            steps {
                // Use the Config File Provider to pull the properties file onto the agent
                configFileProvider([configFile(fileId: 'testenv', variable: 'ENV_PROPS')]) {
                    script {
                        sh "cp ${env.ENV_PROPS} ./src/test/resources/test-config.properties"
                    }
                }
            }
        }

        stage('Test') {
            steps {
                echo 'Running Unit Tests...'
                sh "${GRADLE_BIN} test --no-daemon"
            }
            post {
                always {
                    junit 'build/test-results/test/*.xml'
                }
            }
        }
    }
}
