import knex from 'knex';
import config from '../../knexfile';

const environment = process.env.NODE_ENV || 'development';
const connectionConfig = config[environment];

export const knexInstance = knex(connectionConfig);

export default knexInstance; 