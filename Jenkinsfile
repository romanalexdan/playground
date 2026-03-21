pipeline {
    agent any

    environment {
        // Gradle 9 loves a dedicated user home for caching
        GRADLE_USER_HOME = '/root/.gradle'
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
                sh 'gradle clean assemble --no-daemon'
            }
        }
        stage('Test') {
            steps {
                echo 'Running Unit Tests...'
                sh 'gradle test --no-daemon'
            }
            post {
                always {
                    junit 'build/test-results/test/*.xml'
                }
            }
        }
    }
}
