pipeline {
    agent any

    environment {
            // Gradle 9 loves a dedicated user home for caching
            GRADLE_USER_HOME = "${WORKSPACE}/.gradle"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Build') {
            steps {
                echo 'Starting Gradle Build...'
                sh 'chmod +x gradlew'

                sh './gradlew assemble --no-daemon'
            }
        }
        stage('Test') {
            steps {
                echo 'Running Unit Tests...'
                sh './gradlew test --no-daemon'
            }
            post {
                always {
                    junit 'build/test-results/test/*.xml'
                }
            }
        }
    }
}
