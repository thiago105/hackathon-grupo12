/*ROTA localhost THIAGO*/
const path = require('path')

module.exports = {
    development: {
        client: 'mysql2',
        connection: {
            host: 'localhost',
            user: 'root',
            password: 'root',
            database: 'hackata',
            port: 3306
        },
        migrations: {
            directory: path.resolve(
                __dirname,
                'src',
                'db',
                'knex',
                'migrations'
            )
        }
    }
}