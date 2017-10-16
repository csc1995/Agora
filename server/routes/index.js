const userModule = require('../modules/user')

module.exports = app => {
    app.get('/', function (req, res) {
        return res.send({status: 'up'})
    })

    app.post('/api/login', async function (req, res) {
        try {
            const {username, password} = req.body
            const user = userModule.login({username, password})
            res.send({
                username: user.username
            })
        } catch (error) {
            res.sendStatus(403)
        }


    })
}
