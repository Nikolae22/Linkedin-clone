

import { useState } from 'react'
import { Input } from '../../../../components/input/Input'
import { Box } from '../../components/Box/Box'
import classes from './Profile.module.scss'
import { Button } from '../../../../components/Button/Button'
import { useAuthentication } from '../../contexts/AuthenticationContextProvider'
import { useNavigate } from 'react-router'

export function Profile(){

    const {user , setUser}=useAuthentication();
    const [error,setError]=useState('')
    const [data,setData]=useState({
        firstName:"",
        lastName:"",
        company: "",
        position:"",
        location:""
    })
    const [step,setStep]=useState(0)
    const navigate=useNavigate();

    const submit= async()=>{
        if(!data.firstName || !data.lastName){
            setError("Please fill in your first and last name")
        }
        if(!data.company || !data.position){
            setError("Please fill in your last comp[any and position")
        }

        if(!data.location){
            setError("Plese fill in your lcoation");
            return;
        }

        try{
            const response=await fetch(
                `${import.meta.env.VITE_BASE_URL}/api/v1/authentication/profile/${user?.id}?firstName=${data.firstName}&lastName=${data.lastName}&company${data.company}&position${data.position}&location${data.location}`, {
                    method: "PUT",
                    headers: {
                        Authentcation: `Bearer ${localStorage.getItem("token")}`
                    },
                }
            );
            if(response.ok){
                const updateUser=await response.json();
                setUser(updateUser);
            }else{
                const {message}=await response.json();
                throw new Error(message);
            }
        }catch (e){
            if(e instanceof Error){
                setError(e.message)
            }else{
                setError("An unknown erro occured")
            }
        }finally{
            navigate("/")
        }

    }
    


    return(
        <div className={classes.root}>
            <Box>
                <h1>Only one last step</h1>
                <p>Tell us a bit about yourself so we can personalize your expirence</p>
                {step === 0 &&(
                    <div className={classes.inputs}>
                        <Input
                        onFocus={()=>setError("")}
                        required
                        label='First Name'
                        name='firstName'
                        placeholder='WE'
                        onChange={(e)=>setData((prev)=>({...prev,firstName:e.target.value}))}
                        >
                        </Input>
                        <Input
                        onFocus={()=>setError("")}
                        required
                        label='Last Name'
                        name='lastName'
                        placeholder='WE'
                        onChange={(e)=>setData((prev)=>({...prev,lastName:e.target.value}))}
                        >
                        </Input>
                    </div>
                )}
                {step === 1 && (
                     <div className={classes.inputs}>
                        <Input
                        onFocus={()=>setError("")}
                        label='Latest company'
                        name='company'
                        placeholder='Docker'
                        onChange={(e)=>setData((prev)=>({...prev,company:e.target.value}))}
                        >
                        </Input>
                        <Input
                        onFocus={()=>setError("")}
                        label='Latest position'
                        name='position'
                        placeholder='IT'
                        onChange={(e)=>setData((prev)=>({...prev,position:e.target.value}))}
                        >
                        </Input>
                        </div>
                )}
                {step == 2 && (
                    <Input
                    onFocus={()=>setError("")}
                    label='Location'
                    name='location'
                    placeholder='Italy'
                    onChange={(e)=>setData((prev)=> ({...prev,location:e.target.value}))}
                    >
                    </Input>
                )}
                {error && <p className={classes.error}>{error}</p>}
                <div className={classes.buttons}>
                    {step > 0 && (
                        <Button outline onClick={()=>setStep((prev)=>prev -1)}>
                            Back
                        </Button>
                    )}
                    {step < 2 && (
                        <Button
                        disabled={
                            (step === 0 && (!data.firstName || !data.lastName)) ||
                            (step === 1 && (!data.company || !data.position))
                        }
                        onClick={()=>setStep((prev)=>prev +1)}
                        >Next</Button>
                    )}
                    {step === 2 && (
                        <Button disabled={!data.location} onClick={submit}>
                            Submit
                        </Button>
                    )}
                </div>
            </Box>
        </div>
    )
}