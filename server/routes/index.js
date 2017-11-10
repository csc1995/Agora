const userModule = require('../modules/user')
const signupCodes = require('../modules/signup-codes')
const sessionModule = require('../modules/session')
const proposalsModule = require('../modules/proposals')
const config = require('../config')

const {isAuthenticated} = require('./middleware')

module.exports = app => {
    app.get('/', function (req, res) {
        return res.send({status: 'up'})
    })

    app.post('/api/login', async function (req, res) {
        try {
            const {username, password} = req.body
            const user = await userModule.login({username, password})
            const session = await sessionModule.generateSession({username})
            res.send({
                username: user.username,
                token: session.token
            })
        } catch (error) {
            console.error('error on login', error)
            res.sendStatus(403)
        }
    })

    app.post('/api/signup', async function (req, res) {
        try {
            const {signupCode, username, password, confirmPassword} = req.body
            if (!signupCode || !username || !password || !confirmPassword) throw new Error(`There is an invalid field`)

            if (password !== confirmPassword) throw new Error(`Passwords are different`)

            const isWhitelistedCode = config.constants.whitelistedSignupCodes.includes(signupCode)

            if (!isWhitelistedCode) {
                const code = await signupCodes.getSignupCode(signupCode)
                if (!code) throw new Error(`Code used or not valid`)
            }

            const user = await userModule.createUser({username, password})

            if (!isWhitelistedCode) {
                await signupCodes.useSignupCode({code: signupCode, userId: username})
            }

            const session = await sessionModule.generateSession({username})
            res.send({
                username: user.username,
                token: session.token
            })
        } catch (error) {
            console.error('error on signup', error)
            res.sendStatus(403)
        }

    })

    app.get('/api/profile', isAuthenticated, async function (req, res) {
        try {
            const user = await userModule.getProfile({username: req.username})
            res.json(user)
        } catch (error) {
            console.error('error on getting profile', error)
            res.sendStatus(403)
        }
    })
    
    app.post('/api/profile', isAuthenticated, async function (req, res) {
        try {
            const {cpCode, realname, description, neighborhood, bdate, sex} = req.body
            const user = await userModule.updateProfile({username: req.username, description, cpCode, realname, neighborhood, bdate, sex})
            res.json(user)
        } catch (error) {
            console.error('error on updating profile', error)
            res.sendStatus(403)
        }
    })

    app.delete('/api/proposal/:id', async function (req, res) {
        try {
            const id = req.params.id
            await proposalsModule.deleteProposal(id)
            res.sendStatus(200)
        } catch (error) {
            console.error('error on delete post', error)
            res.sendStatus(403)
        }
    })

    app.delete('/api/logout', isAuthenticated, async function (req, res) {
        try {
            const username = req.username
            const token = req.token
            await sessionModule.deleteSession(username, token)
            res.sendStatus(200)
        } catch (error) {
            console.error('error on logout', error)
            res.sendStatus(403)
        }
    })

}
