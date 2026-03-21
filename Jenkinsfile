pipeline {
    agent any

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

                sh './gradlew build'
            }
        }
        stage('Test') {
            steps {
                echo 'Running Unit Tests...'
                sh './gradlew test'
            }
            post {
                always {
                    junit 'build/test-results/test/*.xml'
                }
            }
        }
    }
}
