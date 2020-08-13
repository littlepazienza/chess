pipeline {
    agent any
    tools {nodejs "node"}
    stages {
        stage('checkout') {
            steps {
                deleteDir()
                checkout scm
            }
        }
        stage('install') {
            steps {
                sh '''
                    npm install
                    npm install electron-packager -g
                '''
            }
        }
        stage('build') {
            steps {
                sh '''
                    electron-packager . chess --platform=darwin --arch=all
                    electron-packager . chess --platform=linux --arch=all
                    zip chess-linux-ia32.zip chess-linux-ia32/*
                    zip chess-linux-x64.zip chess-linux-x64/*
                    zip chess-linux-armv7l.zip chess-linux-armv7l/*
                    zip chess-linux-arm64.zip chess-linux-arm64/*
                    zip chess-darwin-x64.zip chess-darwin-x64/*
                '''
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'chess-*.zip', fingerprint: true
        }
    }
}

