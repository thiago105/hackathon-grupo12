const path = require('path')

module.exports = {
    development: {
        client: 'mysql2',
        connection: {
            host: 'localhost',
            user: 'root',
            password: '',
            database: 'hackata'
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