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
                '''
            }
        }
//         stage('deploy') {
//             steps {
//                 sh '''
//                   git pull --tags
//                   version=$(git describe)
//                   sed -i "s/<!--build_number-->/${version}/g" ./dist/pazienza-tech/index.html
//                   mkdir -p /var/www/html/paz.ienza.tech/$GIT_BRANCH
//                   cp -R ./dist/pazienza-tech/* /var/www/html/paz.ienza.tech/$GIT_BRANCH/
//                 '''
//             }
//         }
    }
}

