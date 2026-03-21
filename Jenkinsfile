pipeline {
    agent any

    environment {
        GRADLE_BIN = '/opt/gradle/bin/gradle'
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
                // Ensure the binary is executable inside the Linux environment
                sh "chmod +x ${GRADLE_BIN}"

                // Verify the version using the direct path
                sh "${GRADLE_BIN} -v"
            }
        }

        stage('Build') {
            steps {
                echo 'Starting Gradle Build...'
                sh "${GRADLE_BIN} clean assemble --no-daemon"
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
